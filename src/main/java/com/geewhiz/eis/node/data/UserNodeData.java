package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.UserAttributes.LDAP_ID;

import java.util.Map;

import com.geewhiz.eis.constants.Label;

public class UserNodeData extends NamedNodeData {

	private String ldapId;

	public UserNodeData(String name) {
		super(name);
	}

	public UserNodeData(String pk, String name) {
		super(pk, name);
	}

	public String getLdapId() {
		return ldapId;
	}

	public void setLdapId(String ldapId) {
		this.ldapId = ldapId;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(LDAP_ID, getLdapId());

		return attributeValues;
	}

	@Override
	public Label getLabel() {
		return Label.USER;
	}

}
