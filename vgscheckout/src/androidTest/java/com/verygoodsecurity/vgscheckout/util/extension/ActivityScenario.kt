package com.verygoodsecurity.vgscheckout.util.extension

import android.app.Instrumentation
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import org.junit.Assert

/**
 * Used to fix activity scenario get result race condition.
 *
 * Read more here: https://github.com/android/android-test/issues/676.
 */
val ActivityScenario<*>.safeResult: Instrumentation.ActivityResult
    get() {
        awaitBlock { state == Lifecycle.State.DESTROYED } // await for the activity to be destroyed
        return this.result // this will return quick as the result is already retrieved
    }

// util function to retry and await until the block is true or the timeout is reached
private fun awaitBlock(timeOut: Int = 10_000, block: () -> Boolean) {
    val start = System.currentTimeMillis()
    var value = block.invoke()
    while (!value && System.currentTimeMillis() < start + timeOut) {
        Thread.sleep(50)
        value = block.invoke()
    }
    Assert.assertTrue("Couldn't await the condition", value)
}