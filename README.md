[![UT](https://img.shields.io/badge/Unit_Test-pass-green)]()
[![license](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/verygoodsecurity/vgs-collect-android/blob/master/LICENSE)


# VGS Checkout

VGS provides you with a Universal Checkout and User Experience which is fully integrated with our payment optimization solution. 
We offer a single, customized, consistent experience to your customers across mobile devices with Android OS. 


Table of contents
=================

<!--ts-->
   * [Structure](#structure)
   * [Integration](#integration)
   * [Next steps](#next-steps)
   * [License](#license)
<!--te-->

<p align="center">
<img src="/img/vgs-checkout-android-state.png" width="200" alt="VGS Checkout States" hspace="20"><img src="/img/vgs-checkout-android-response.png" width="200" alt="VGS Checkout Response" hspace="20">
</p>



## Structure
* **custom-example** - sample application of VGS Checkout with custom configuration.
* **multiplexing-example** - sample application of VGS Checkout with multiplexing configuration for communication with payment optimization solution.
* **vgscheckout** - provides an API for interacting with the VGS services.


## Integration
For integration you need to install the [Android Studio](http://developer.android.com/sdk/index.html) and a [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) on your machine.






<table>
  <tr>
    <td colspan="2">
      <b>Integrate the VGS Checkout to your project</b>. <br/>
      If you are using Maven, add the following to your <code>build.gradle</code> file.
    </td>
  </tr>
  <tr>
    <td colspan="2">

```gradle
dependencies {
    implementation "com.verygoodsecurity:checkout:<latest-version>"
}
```
  </td>
  </tr>

      
      

  <tr>
    <td>
      <b> To initialize VGS Checkout you have to set your <a href="https://www.verygoodsecurity.com/docs/payment-optimization/multiplexing/api/authentication/">access token</a>, <a href="https://www.verygoodsecurity.com/docs/terminology/nomenclature#vault">vault id</a> and <a href="https://www.verygoodsecurity.com/docs/getting-started/going-live#sandbox-vs-live">environment</a> type.</b> 
      </br>You can find additional information about VGS Checkout configurations at the following <a href="https://www.verygoodsecurity.com/docs/payment-optimization/checkout/android-sdk/configuration/">section</a>.
    </td>
     <th rowspan="2"><img src="/img/vgs-field-setup-state.gif"></th>
  </tr>
  <tr>
    <td>

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkout = VGSCheckout(this)
    }

    private fun presentVGSCheckout() {
        checkout.present("<ACCESS_TOKEN>", "<VAULT_ID>", VGSCheckoutEnvironment.Sandbox())
    }
}
```
  </td>
  </tr>




  <tr>
    <td> 
      <b> In case you need retrieve responses, put <code>VGSCheckoutCallback</code> as a second parameter during initialization <code>VGSCheckout</code>.      
    </td>
     <th rowspan="2"><img src="/img/vgs-response-state.gif"></th>
  </tr>
  <tr>
    <td>

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var checkout: VGSCheckout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkout = VGSCheckout(this, object : VGSCheckoutCallback {

            override fun onCheckoutResult(result: VGSCheckoutResult) {
                when (result) {
                    is VGSCheckoutResult.Success -> {
                        // TODO: Handle success
                    }
                    is VGSCheckoutResult.Failed -> {
                        // TODO: Handle failed
                    }
                    is VGSCheckoutResult.Canceled -> {
                        // TODO: Handle canceled
                    }
                }
            }
        })
    }

    private fun presentVGSCheckout() {
        checkout.present("<ACCESS_TOKEN>", "<VAULT_ID>", VGSCheckoutEnvironment.Sandbox())
    }
}
```
  </td>
  </tr>

</table>








## Next steps
Check out documentation guides:
-  <a href="https://www.verygoodsecurity.com/docs/payment-optimization/checkout/android-sdk">Overview</a>
-  <a href="https://www.verygoodsecurity.com/docs/payment-optimization/checkout/android-sdk/integration">Integration with payment optimization solution</a>
-  <a href="https://www.verygoodsecurity.com/docs/payment-optimization/checkout/android-sdk/configuration">How to configure VGS Checkout</a>

For a quick start, try our <a href="https://github.com/vgs-samples/vgs-checkout-android">Demo application</a>.


## License
VGS Checkout is released under the MIT license. [See LICENSE](https://github.com/verygoodsecurity/vgs-checkout-android/blob/master/LICENSE) for details.
