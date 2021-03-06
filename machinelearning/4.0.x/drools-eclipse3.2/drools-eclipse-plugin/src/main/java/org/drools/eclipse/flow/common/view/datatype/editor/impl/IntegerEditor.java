package org.drools.eclipse.flow.common.view.datatype.editor.impl;
/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;import org.drools.eclipse.flow.common.view.datatype.editor.Editor;
import org.drools.ruleflow.common.datatype.DataType;
import org.drools.ruleflow.common.datatype.impl.type.IntegerDataType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * Default integer editor.
 * 
 * @author <a href="mailto:kris_verlaenen@hotmail.com">Kris Verlaenen</a>
 */
public class IntegerEditor extends Composite implements Editor {

    private Text text;
    
    public IntegerEditor(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new FillLayout());
        text = new Text(this, SWT.NONE);
    }

    public void setDataType(DataType dataType) {
        if (!(dataType instanceof IntegerDataType)) {
            throw new IllegalArgumentException("Illegal data type " + dataType);
        }
    }

    public Serializable getValue() {
        String valueString = text.getText();
        if ("".equals(valueString)) {
            return null;
        }
        try {
            return new Integer(valueString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The value " + valueString
                + " is not a valid integer.");
        }
    }

    public void setValue(Serializable value) {
        if (value == null) {
            text.setText("");
        } else if (value instanceof Integer) {
            text.setText(((Integer) value).toString());
        } else {
            throw new IllegalArgumentException("Value must be an integer: " + value);
        }
    }
    
    public void reset() {
        text.setText("");
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        text.setEnabled(enabled);
    }
}
