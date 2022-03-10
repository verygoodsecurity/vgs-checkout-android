package com.verygoodsecurity.vgscheckout.collect.core.storage

import com.verygoodsecurity.vgscheckout.collect.core.model.state.FieldContent.*
import com.verygoodsecurity.vgscheckout.collect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.field.FieldStateContractor
import com.verygoodsecurity.vgscheckout.collect.core.storage.content.field.TemporaryFieldsStorage
import com.verygoodsecurity.vgscheckout.collect.view.VGSCollectView
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.CountryNameToIsoSerializer
import com.verygoodsecurity.vgscheckout.collect.view.core.serializers.VGSExpDateSeparateSerializer

/** @suppress */
internal class InternalStorage {
    private val fieldsDependencyDispatcher: DependencyDispatcher

    private val fieldsStorage: VgsStore<Int, VGSFieldState>
    private val emitter: IStateEmitter

    init {
        fieldsDependencyDispatcher = Notifier()

        val fieldStateContractor = FieldStateContractor()
        with(TemporaryFieldsStorage(fieldStateContractor)) {
            attachFieldDependencyObserver(fieldsDependencyDispatcher)

            fieldsStorage = this
            emitter = this
        }
    }

    fun getFieldsStorage() = fieldsStorage

    fun getFieldsStates(): MutableCollection<VGSFieldState> = fieldsStorage.getItems()

    fun getAssociatedList(fieldsIgnore: Boolean = false): MutableCollection<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()

        if (fieldsIgnore.not()) {
            list.addAll(stateToAssociatedList(fieldsStorage.getItems()))
        }

        return list
    }

    fun clear() {
        fieldsStorage.clear()
    }

    fun attachStateChangeListener(fieldStateListener: OnFieldStateChangeListener?) {
        emitter.attachStateChangeListener(fieldStateListener)
    }

    fun performSubscription(view: VGSCollectView?) {
        view?.let {
            fieldsDependencyDispatcher.addDependencyListener(
                it.getFieldType(),
                it.statePreparer.getDependencyListener()
            )
            it.addStateListener(emitter.performSubscription())
        }
    }

    fun unsubscribe(view: VGSCollectView?) {
        view?.let {
            it.statePreparer.unsubscribe()
            fieldsStorage.remove(it.statePreparer.getView().id)
        }
    }

    private fun stateToAssociatedList(items: MutableCollection<VGSFieldState>): MutableCollection<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        items.filter { state -> state.isNotNullOrEmpty() }.forEach { state ->
            with(state.content!!) {
                when (this) {
                    is CardNumberContent -> result.add(state.fieldName!! to (rawData ?: data!!))
                    is SSNContent -> result.add(state.fieldName!! to (rawData ?: data!!))
                    is CreditCardExpDateContent -> {
                        result.addAll(handleExpirationDateContent(state.fieldName!!, this))
                    }
                    is InfoContent -> result.add(state.fieldName!! to handleInfoContent(this))
                }
            }
        }
        return result
    }

    private fun handleExpirationDateContent(
        fieldName: String,
        content: CreditCardExpDateContent
    ): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val data = (content.rawData ?: content.data!!)
        if (content.serializers != null) {
            content.serializers?.forEach {
                if (it is VGSExpDateSeparateSerializer) {
                    result.addAll(
                        it.serialize(VGSExpDateSeparateSerializer.Params(data, content.dateFormat))
                    )
                }
            }
        } else {
            result.add(fieldName to data)
        }
        return result
    }

    private fun handleInfoContent(content: InfoContent): String {
        val data = (content.rawData ?: content.data!!)
        return (content.serializer as? CountryNameToIsoSerializer)?.serialize(data) ?: data
    }
}