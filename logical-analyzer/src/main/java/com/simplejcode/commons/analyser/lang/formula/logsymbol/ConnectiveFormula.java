package com.simplejcode.commons.analyser.lang.formula.logsymbol;

import com.simplejcode.commons.analyser.lang.Token;
import com.simplejcode.commons.analyser.lang.formula.*;

public class ConnectiveFormula extends LogicalSymbolFormula {
    protected Formula[] members;

    public ConnectiveFormula(Token token, int nOfMembers) {
        super(token);
        members = new Formula[nOfMembers];
    }

    public Formula[] members() {
        return members;
    }

    public Formula getMember(int index) {
        return members[index];
    }

    public void setMember(int index, Formula f) {
        members[index] = f;
    }

    public String infixForm() {
        String line = value.toString();
        if (members != null) {
            for (Formula child : members) {
                line += " " + child.toString();
            }
        }
        return line;
    }

    public boolean equals(Object obj) {
        /*if(!(obj instanceof Formula)) {
            return false;
        }*/
        return toString().equals(obj.toString());
    }

    public String toString() {
        return withoutBrackets(0);
    }

    public String withBrackets() {
        switch (members.length) {
            case 1:
                return Token.inTheBrackets(value + members[0].withBrackets());
            case 2:
                return Token.inTheBrackets(members[0].withBrackets() +
                        " " + value + " " +
                        members[1].withBrackets());
            default:
                return null;
        }
    }

    public String withoutBrackets(int parentPriority) {
        int priority = value.getPriority();
        String line = value.toString();
        switch (members.length) {
            case 1:
                line += members[0].withoutBrackets(priority);
                break;
            case 2:
                line += " " + members[1].withoutBrackets(priority + 1);
                line = members[0].withoutBrackets(priority) + " " + line;
                break;
            default:
                return null;
        }
        return priority < parentPriority ? Token.inTheBrackets(line) : line;
    }
}
