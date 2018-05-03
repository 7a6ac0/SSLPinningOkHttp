# SSLPinningOkHttp

OkHttp透過憑證綁定方式作連線，在這是綁定[Github](https://github.com)的憑證，當使用Proxy(Ex. [Charles](https://www.charlesproxy.com/))攔截傳輸內容時會無法正常連線。

## CertificatePinner
使用```CertificatePinner```加入Github網站公鑰
```java
CertificatePinner certPinner = new CertificatePinner.Builder()
                        .add("github.com",
                                "sha256/pL1+qb9HTMRZJmuC/bB/ZI9d302BYrrqiVuRyW+DGrU=")
                        .add("github.com",
                                "sha256/RRM1dGqnDFsCJXBTHky16vi1obOlCgFFn/yOhI/y+ho=")
                        .build();
```

在OkHttp新增憑證綁定
```java
OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .certificatePinner(mCertPinner)
                .build();
```

## Network Security Config
Android API 24以後才有的機制，利用```script/cert.sh```取得網站公鑰。
```bash
root@debian:~# ./script/cert.sh github.com
/businessCategory=Private Organization/1.3.6.1.4.1.311.60.2.1.3=US/1.3.6.1.4.1.311.60.2.1.2=Delaware/serialNumber=5157550/street=88 Colin P Kelly, Jr Street/postalCode=94107/C=US/ST=California/L=San Francisco/O=GitHub, Inc./CN=github.com
pL1+qb9HTMRZJmuC/bB/ZI9d302BYrrqiVuRyW+DGrU=
/C=US/O=DigiCert Inc/OU=www.digicert.com/CN=DigiCert SHA2 Extended Validation Server CA
RRM1dGqnDFsCJXBTHky16vi1obOlCgFFn/yOhI/y+ho=
```

將取得的公鑰加入[network_security_config.xml](https://github.com/7a6ac0/SSLPinning/blob/master/app/src/main/res/xml/network_security_config.xml)
