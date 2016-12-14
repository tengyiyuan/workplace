/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.toplion.cplusschool.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088121425593701";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "zhaoyh@toplion.com.cn";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICXgIBAAKBgQDIibkLb9K9+uLVeazPCal9OiF2Go5AQ1P1uOIWnAGvGhvTpSfLYX6DbkgT5Q1MWRU22nl7xn5bG9Hr149Q1Vo/M22EzdZn7SRw/DIpkh5fljF0y1ETuomo55vYYF7T1Mk37LfO3k1hwUrSvLjZzkDpXAe9Qs1VGD6psY1shl3IWQIDAQABAoGBAMIBSv2ffvfAQbZYcjARnuDBXiHN6xSMjitIY6GP56kozwbf3jHJ7gTkqeJlU4orHHTmw2RVUWR+84UAE2wWG6kWfRfIwcSm2yvUMONXkOi7zWCXolvCs5pe45aleJTwSNR9bwBtRHj4M9m0fWBk/sfgcnFb/lziWsrzS1Bg6IkBAkEA5aEImDVq2NAx9CUU0qjJpxfO17vw/UqpHg3rm/E0MRgB4LWkYdyjKQu0VUt5n4y5l+dBybNh8rRIzlfREMJEiQJBAN+RbaRYXD8CMmNK8vEz/uE1v84VmjxMv1GohOu12Utvv5Qz++fhdfQJdS51aD+RmIxbQ6t+SrbHZj/RUctREVECQQCcV+jvISePWSk5zmmACJXLo6UcM4UgH85HfTjWowJjDrJwmZvDWaLpVmPA0zOK0xil+TlMqErfIaqUeywIGgzpAkEAvKA4v5aFjevOk5Pi0bsq6tysjbYQQZwHN2BVdYIiadFI9EO/3+6L1HA+XApXAHtZF+LQf1q4suodzO5QcphdYQJAXxmjjVaDI0urwIMWeN0pJqi0rF6EA1d6Dd5foFHayR97N959bov/+sqgbnmy6kG32RDrKUSRD/BlwY3oX6W3/w==";
	//公钥
	public static final String PUBLIC = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMiJuQtv0r364tV5rM8JqX06IXYajkBDU/W44hacAa8aG9OlJ8thfoNuSBPlDUxZFTbaeXvGflsb0evXj1DVWj8zbYTN1mftJHD8MimSHl+WMXTLURO6iajnm9hgXtPUyTfst87eTWHBStK8uNnOQOlcB71CzVUYPqmxjWyGXchZAgMBAAECgYEAwgFK/Z9+98BBtlhyMBGe4MFeIc3rFIyOK0hjoY/nqSjPBt/eMcnuBOSp4mVTiiscdObDZFVRZH7zhQATbBYbqRZ9F8jBxKbbK9Qw41eQ6LvNYJeiW8Kzml7jlqV4lPBI1H1vAG1EePgz2bR9YGT+x+BycVv+XOJayvNLUGDoiQECQQDloQiYNWrY0DH0JRTSqMmnF87Xu/D9SqkeDeub8TQxGAHgtaRh3KMpC7RVS3mfjLmX50HJs2HytEjOV9EQwkSJAkEA35FtpFhcPwIyY0ry8TP+4TW/zhWaPEy/UaiE67XZS2+/lDP75+F19Al1LnVoP5GYjFtDq35KtsdmP9FRy1ERUQJBAJxX6O8hJ49ZKTnOaYAIlcujpRwzhSAfzkd9ONajAmMOsnCZm8NZoulWY8DTM4rTGKX5OUyoSt8hqpR7LAgaDOkCQQC8oDi/loWN686Tk+LRuyrq3KyNthBBnAc3YFV1giJp0Uj0Q7/f7ovUcD5cClcAe1kX4tB/Wriy6h3M7lBymF1hAkBfGaONVoMjS6vAgxZ43SkmqLSsXoQDV3oN3l+gUdrJH3s33n1ui//6yqBuebLqQbfZEOspRJEP8GXBjehfpbf/";

}
