package com.geewhiz.eis.constants;

import org.neo4j.graphdb.RelationshipType;

public enum Relation implements RelationshipType
{
	SUCCESSOR, BASED_ON, CONTAINS, CREATED_BY, MODIFIED_BY, EFFECTIVE_SINCE, APPLIES_TO, VALUE_OF, WAS_INTRODUCED, OVERWRITES
}
