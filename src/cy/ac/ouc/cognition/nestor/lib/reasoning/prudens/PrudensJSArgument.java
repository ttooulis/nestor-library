package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import cy.ac.ouc.cognition.nestor.lib.reasoning.ReasoningObject;

public class PrudensJSArgument extends ReasoningObject {
	
	 private int index;
	 private String name;
	 private boolean isAssigned;
	 private boolean isList;
	 private boolean isExpression;
	 private String value;
	 private boolean muted;


	 // Getter Methods 

	 public int getIndex() {
	  return index;
	 }

	 public String getName() {
	  return name;
	 }

	 public boolean getIsAssigned() {
	  return isAssigned;
	 }

	 public boolean getIsList() {
		  return isList;
		 }

	 public boolean getIsExpression() {
		  return isExpression;
		 }

	 public String getValue() {
	  return value;
	 }

	 public boolean getMuted() {
	  return muted;
	 }

	 // Setter Methods 

	 public void setIndex(int index) {
	  this.index = index;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 public void setIsAssigned(boolean isAssigned) {
	  this.isAssigned = isAssigned;
	 }

	 public void setIsList(boolean isList) {
		  this.isList = isList;
		 }

	 public void setIsExpression(boolean isExpression) {
		  this.isExpression = isExpression;
		 }

	 public void setValue(String value) {
	  this.value = value;
	 }

	 public void setMuted(boolean muted) {
	  this.muted = muted;
	 }

}
