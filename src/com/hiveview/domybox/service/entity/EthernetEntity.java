package com.hiveview.domybox.service.entity;

public class EthernetEntity {

	private String ifName;
	private String connectMode;
	private String ip;
	private String route;
	private String dns;
	private String netMask;
	public String getIfName() {
		return ifName;
	}
	public void setIfName(String ifName) {
		this.ifName = ifName;
	}
	public String getConnectMode() {
		return connectMode;
	}
	public void setConnectMode(String connectMode) {
		this.connectMode = connectMode;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getDns() {
		return dns;
	}
	public void setDns(String dns) {
		this.dns = dns;
	}
	public String getNetMask() {
		return netMask;
	}
	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}
}
