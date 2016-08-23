// KeyStoreUtil.getModulus()  
var rsaModules = '98f02b0b806ab133d64d3dd91b7a4618e0a86cbcf2a84a76d586ba70334287d3578afceba30747e982e82b99b304ffc4812f6a867cd7ff543a5c8134b63cd45e711417d1b1607d416e9dc6d227399071378c4fc54d3637dde5c13738b4cc23d9e92491a536776ba78fe1d44c436f0976d60136b22453fb7ec34648bc73486d0f';
//KeyStoreUtil.getPublicExponent()
var rsaPublicExponent = '10001';
var RSA_key = initRSA(rsaModules, rsaPublicExponent);
var encryptedPassword = encryptedString(RSA_key, encodeURI('123456'));