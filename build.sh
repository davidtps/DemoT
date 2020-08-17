#! /bin/sh
./gradlew clean
./gradlew aR;
java  -jar ../../tools/MTK5660_signature_tool_for_hisense_app/signapk.jar ../../tools/MTK5660_signature_tool_for_hisense_app/platform.x509.pem ../../tools/MTK5660_signature_tool_for_hisense_app/platform.pk8 ./apk/StoreMode_usa6101eu_release.apk ./apk/StoreMode_signed.apk
