/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\kylin\\android\\DomyBoxLauncher\\src\\android\\net\\ethernet\\IEthernetManager.aidl
 */
package android.net.ethernet;
public interface IEthernetManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements android.net.ethernet.IEthernetManager
{
private static final java.lang.String DESCRIPTOR = "android.net.ethernet.IEthernetManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an android.net.ethernet.IEthernetManager interface,
 * generating a proxy if needed.
 */
public static android.net.ethernet.IEthernetManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof android.net.ethernet.IEthernetManager))) {
return ((android.net.ethernet.IEthernetManager)iin);
}
return new android.net.ethernet.IEthernetManager.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getDeviceNameList:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getDeviceNameList();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_setEthState:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setEthState(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getEthState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getEthState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_UpdateEthDevInfo:
{
data.enforceInterface(DESCRIPTOR);
android.net.ethernet.EthernetDevInfo _arg0;
if ((0!=data.readInt())) {
_arg0 = android.net.ethernet.EthernetDevInfo.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.UpdateEthDevInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isEthConfigured:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isEthConfigured();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getSavedEthConfig:
{
data.enforceInterface(DESCRIPTOR);
android.net.ethernet.EthernetDevInfo _result = this.getSavedEthConfig();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_getTotalInterface:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getTotalInterface();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setEthMode:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setEthMode(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements android.net.ethernet.IEthernetManager
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String[] getDeviceNameList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDeviceNameList, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setEthState(int state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(state);
mRemote.transact(Stub.TRANSACTION_setEthState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getEthState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEthState, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void UpdateEthDevInfo(android.net.ethernet.EthernetDevInfo info) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((info!=null)) {
_data.writeInt(1);
info.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_UpdateEthDevInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isEthConfigured() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEthConfigured, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public android.net.ethernet.EthernetDevInfo getSavedEthConfig() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
android.net.ethernet.EthernetDevInfo _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSavedEthConfig, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = android.net.ethernet.EthernetDevInfo.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getTotalInterface() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getTotalInterface, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setEthMode(java.lang.String mode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(mode);
mRemote.transact(Stub.TRANSACTION_setEthMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getDeviceNameList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_setEthState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getEthState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_UpdateEthDevInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isEthConfigured = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getSavedEthConfig = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getTotalInterface = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setEthMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
public java.lang.String[] getDeviceNameList() throws android.os.RemoteException;
public void setEthState(int state) throws android.os.RemoteException;
public int getEthState() throws android.os.RemoteException;
public void UpdateEthDevInfo(android.net.ethernet.EthernetDevInfo info) throws android.os.RemoteException;
public boolean isEthConfigured() throws android.os.RemoteException;
public android.net.ethernet.EthernetDevInfo getSavedEthConfig() throws android.os.RemoteException;
public int getTotalInterface() throws android.os.RemoteException;
public void setEthMode(java.lang.String mode) throws android.os.RemoteException;
}
