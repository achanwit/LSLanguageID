package com.omniscien.lslanguageid.model;

import java.util.Collection;

public class LanguageidRespond extends ResponseMessage {
	private Collection<LanguageidModel> languageid;
	
	public Collection<LanguageidModel> getLanguageid() {
		return languageid;
	}
	
	public void setLanguageid(Collection<LanguageidModel> languageid) {
		this.languageid = languageid;
	}
	
	@Override
	public String toString() {
		return "LanguageidResponseMessage [languageid=" + languageid == null ? "null" : languageid.toString() + "]";
	}

}
