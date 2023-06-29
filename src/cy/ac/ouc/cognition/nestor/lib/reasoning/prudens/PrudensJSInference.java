package cy.ac.ouc.cognition.nestor.lib.reasoning.prudens;

import java.util.ArrayList;

import cy.ac.ouc.cognition.nestor.lib.reasoning.ReasoningObject;

public class PrudensJSInference extends ReasoningObject {
	
	 private String name;
	 private boolean sign;
	 private boolean isJS;
	 private boolean isEquality;
	 private boolean isInequality;
	 private boolean isAction;
	 ArrayList < PrudensJSArgument > args = new ArrayList < PrudensJSArgument > ();
	 private int arity;


	 // Getter Methods 

	 public String getName() {
	  return name;
	 }

	 public boolean getSign() {
	  return sign;
	 }

	 public boolean getIsJS() {
	  return isJS;
	 }

	 public boolean getIsEquality() {
	  return isEquality;
	 }

	 public boolean getIsInequality() {
	  return isInequality;
	 }

	 public boolean getIsAction() {
	  return isAction;
	 }

	 /**
	  * @param args the args to set
	  */
	 public void setArgs(ArrayList<PrudensJSArgument> args) {
		 this.args = args;
	 }

	 public int getArity() {
	  return arity;
	 }

	 // Setter Methods 

	 public void setName(String name) {
	  this.name = name;
	 }

	 public void setSign(boolean sign) {
	  this.sign = sign;
	 }

	 public void setIsJS(boolean isJS) {
	  this.isJS = isJS;
	 }

	 public void setIsEquality(boolean isEquality) {
	  this.isEquality = isEquality;
	 }

	 public void setIsInequality(boolean isInequality) {
	  this.isInequality = isInequality;
	 }

	 public void setIsAction(boolean isAction) {
	  this.isAction = isAction;
	 }

	 /**
	 * @return the args
	 */
	 public ArrayList<PrudensJSArgument> getArgs() {
	 	return args;
	 }

	 public void setArity(int arity) {
	  this.arity = arity;
	 }

}
