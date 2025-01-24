# Table of Contents

[toc]


## KEYS

### DUKPT
###### add by Simon

    com/verifone/demo/emv/transaction/TransBasic.java
    com/vfi/smartpos/deviceservice/aidl/key_manager/IDukpt.aidl

    public void doPinPad(boolean isOnlinePin, int retryTimes)
    bRet = iDukpt.loadDukptKey(1, Utility.hexStr2Byte(ksn), Utility.hexStr2Byte(key), null, extend);
    param.putInt(ConstIPinpad.startPinInput.param.KEY_desType_int, ConstIPinpad.startPinInput.param.Value_desType_DUKPT_3DES);
    ipinpad.startPinInput(1, param, globeParam, pinInputListener);



## Serial
### POS 2 PC via micro/type c cable
######  add by Simon
[Settings] -> [SerialPort] : [USB UART] 

com/verifone/demo/emv/SerialPortActivity.java

### POS 2 POS
######  add by Simon
[Settings] -> [SerialPort] :
[OTG USB2Serial] for Master, using the OTG cable + micro/type c cable to connect slave

	Open port will fails if it's not a valid OTG cable
        
[USB UART] for slave,

com/verifone/demo/emv/SerialPortActivity.java
