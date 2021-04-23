package model;

import java.util.Collection;

public class LanguageidRespond extends ResponseMessage {
	private Collection<Languageid> languageid;
	
	public Collection<Languageid> getLanguageid() {
		return languageid;
	}
	
	public void setLanguageid(Collection<Languageid> languageid) {
		this.languageid = languageid;
	}
	
	@Override
	public String toString() {
		return "LanguageidResponseMessage [languageid=" + languageid == null ? "null" : languageid.toString() + "]";
	}

}
