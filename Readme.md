# Socorro  
An android library allowing developers to use local API response to mock and test Retrofit code. With very minimal change in the code for calling the repositories or the retrofit interface, Socorro can be used in its place. It uses the RxJava implementation.
  
**Usage**  
You can download the library from Jitpack. Mention the following in the gradle file.  
```  
implementation ''  
```  

**History**  
Sometimes for developers, they might not have received a funtional API which can return the data and for which they need to write various boilerplate code to retrive the data from local files. Another scenario is to test how the application behaves based on various combinations of response for an API. To address this, Socorro is developed.  

**Using it**  
Consider the retrofit call which makes an actual API call over network to get the response  
```  
userInfo.getInfo(userId)  
   .observeOn(AndroidSchedulers.mainThread())  
   .subscribeOn(Schedulers.io())  
   .subscribeWith(...)
```  

The same code with Socorro will be translated to  
```  
Socorro<UserResponse>.mockWith(config, context, UserResponse::class.java)  
   .observeOn(AndroidSchedulers.mainThread())  
   .subscribeOn(Schedulers.io())  
   .subscribeWith(...)
```  

So, the basic call remains very much the same, only the executor is changed. The driving force for the flixibity of this library is SocorroConfig. This allows the developer to just modify the config and based on which Socorro will behave.  
  
**SocorroConfig**  
Conside the configuration setup below:  
```  
val config = SocorroConfig.createWith()
    .endPoint("simple/user/mock")
    .putCodeResponseMap(200, "simple_user_mock.json")
    .sourceFileFrom(SocorroConfig.SourceFileFrom.Assets)
    .responseCode(200)
    .delay(100)
    .success(true)
```  
The configuration specifies Socorro that it needs to read the file `simple_user_mock.json` from the assets folder of the application and return success after a delay of 100 milliseconds. It also specifies that the returned `Retrofit.Response` should have the response code 200.  

So this setup looks simple, but consider that the end point returns multiple response codes, for example conside 288. Then in that case the developer just needs to set `responseCode(288)` and Socorro will return the appropriate `UserResponse` object with the corresponding response.  

**Code Response Map**  
Calling `putCodeResponseMap` is not mandatory. It needs to be only called when the developer does not want to follow the default naming convention. The default naming convention is:  
> endpoint_responseCode.json  

Hence in the above example, Socorro would try to find the below file under assets folder:  
> simple_user_mock_288.json  

But incase if the user wants to read a file with different name, then `putCodeResponseMap` needs to be called. For example:  
> putCodeResponseMap(288, "user_info_with_image.json")  

**Reading File Location**  
Socorro can read the response files from 3 location:
* Assets  
* Resources  
* Raw  

This can be set by calling the `sourceFileFrom` method in the config.
