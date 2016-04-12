package eu.concord.commands.impl;

import eu.concord.commands.interfaces.ArgumentVerifier;

public class DefaultArgumentVerifier implements ArgumentVerifier<Object> {

    public boolean verify(Object object) {
        return true;
    }

}
