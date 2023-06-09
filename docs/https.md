### PKCS 12 File Generation

Java can only use `jks` or `pkcs 12` files in keystore. To generate use
```bash
openssl pkcs12 -export -password pass:<password> -name client -in <public key file> -out <pkcs 12 file> -inkey <key file>
```

Will need to use ase 64
```bash
openssl base64 -in "${targetPath}/client_keystore.p12" -out "${targetPath}/client_keystore.b64"
```