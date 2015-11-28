keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -validity 30 -keysize 2048
keytool -export -keystore keystore.jks -alias selfsigned -file notlet.cer
