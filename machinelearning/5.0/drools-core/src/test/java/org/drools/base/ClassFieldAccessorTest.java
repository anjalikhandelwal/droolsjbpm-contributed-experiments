package org.drools.base;

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

import junit.framework.Assert;
import junit.framework.TestCase;

import org.drools.RuntimeDroolsException;
import org.drools.util.asm.BeanInherit;
import org.drools.util.asm.InterfaceChild;
import org.drools.util.asm.TestAbstract;
import org.drools.util.asm.TestAbstractImpl;
import org.drools.util.asm.TestBean;
import org.drools.util.asm.TestInterface;
import org.drools.util.asm.TestInterfaceImpl;

public class ClassFieldAccessorTest extends TestCase {

    private ClassFieldAccessorCache cache;

    protected void setUp() throws Exception {
        super.setUp();
        cache = ClassFieldAccessorCache.getInstance();
    }

    public void testBasic() throws Exception {
        final Object[] objArray = new Object[1];

        final TestBean obj = new TestBean();
        obj.setBlah( false );
        obj.setSomething( "no" );
        obj.setObjArray( objArray );

        final ClassFieldReader ext = cache.getReader( TestBean.class,
                                                      "blah",
                                                      getClass().getClassLoader() );
        assertEquals( false,
                      ((Boolean) ext.getValue( null,
                                               obj )).booleanValue() );

        final ClassFieldReader ext2 = cache.getReader( TestBean.class,
                                                       "fooBar",
                                                       getClass().getClassLoader() );
        assertEquals( "fooBar",
                      ext2.getValue( null,
                                     obj ) );

        final ClassFieldReader ext3 = cache.getReader( TestBean.class,
                                                       "objArray",
                                                       getClass().getClassLoader() );
        assertEquals( objArray,
                      ext3.getValue( null,
                                     obj ) );

    }

    public void testInterface() throws Exception {

        final TestInterface obj = new TestInterfaceImpl();
        final ClassFieldReader ext = cache.getReader( TestInterface.class,
                                                      "something",
                                                      getClass().getClassLoader() );

        assertEquals( "foo",
                      (String) ext.getValue( null,
                                             obj ) );

    }

    public void testAbstract() throws Exception {

        final ClassFieldReader ext = cache.getReader( TestAbstract.class,
                                                      "something",
                                                      getClass().getClassLoader() );
        final TestAbstract obj = new TestAbstractImpl();
        assertEquals( "foo",
                      (String) ext.getValue( null,
                                             obj ) );

    }

    public void testInherited() throws Exception {
        final ClassFieldReader ext = cache.getReader( BeanInherit.class,
                                                      "text",
                                                      getClass().getClassLoader() );
        final BeanInherit obj = new BeanInherit();
        assertEquals( "hola",
                      (String) ext.getValue( null,
                                             obj ) );

    }

    public void testMultipleInterfaces() throws Exception {
        final ConcreteChild obj = new ConcreteChild();
        final ClassFieldReader ext = cache.getReader( InterfaceChild.class,
                                                      "foo",
                                                      getClass().getClassLoader() );
        assertEquals( 42,
                      ((Number) ext.getValue( null,
                                              obj )).intValue() );
    }

    public void testLong() throws Exception {
        final ClassFieldReader ext = cache.getReader( TestBean.class,
                                                      "longField",
                                                      getClass().getClassLoader() );
        final TestBean bean = new TestBean();
        assertEquals( 424242,
                      ((Number) ext.getValue( null,
                                              bean )).longValue() );
    }

    public void testNonExistentField() throws Exception {
        final Object[] objArray = new Object[1];

        final TestBean obj = new TestBean();
        obj.setBlah( false );
        obj.setSomething( "no" );
        obj.setObjArray( objArray );

        try {
            final ClassFieldReader ext = cache.getReader( TestBean.class,
                                                          "xyz",
                                                          getClass().getClassLoader() );
            fail( "A RuntimeDroolsException should have been raised" );
        } catch ( RuntimeDroolsException e ) {
            // everything is fine, since field does not exist
            // e.printStackTrace();
        } catch ( Exception e ) {
            fail( "A RuntimeDroolsException should have been raised" );
        }

    }

    public void testBuildFieldAccessor() {
        try {
            ClassFieldAccessor intAccessor = cache.getAccessor( TestClass.class,
                                                                "intAttr",
                                                                getClass().getClassLoader() );
            ClassFieldAccessor strAccessor = cache.getAccessor( TestClass.class,
                                                                "strAttr",
                                                                getClass().getClassLoader() );

            String testString1 = "TestAttr1";
            String testString2 = "TestAttr2";
            TestClass instance = new TestClass();
            instance.setIntAttr( 10 );
            instance.setStrAttr( testString1 );

            Assert.assertEquals( "Error reading int attr",
                                 10,
                                 ((Integer) intAccessor.getValue( instance )).intValue() );
            Assert.assertEquals( "Error reading int attr",
                                 10,
                                 intAccessor.getIntValue( instance ) );
            Assert.assertEquals( "Error reading String attr",
                                 testString1,
                                 strAccessor.getValue( instance ) );

            intAccessor.setValue( instance,
                                  new Integer( 50 ) );
            strAccessor.setValue( instance,
                                  testString2 );

            Assert.assertEquals( "Error setting int attr",
                                 50,
                                 instance.getIntAttr() );
            Assert.assertEquals( "Error setting String attr",
                                 testString2,
                                 instance.getStrAttr() );

            intAccessor.setIntValue( instance,
                                     40 );
            Assert.assertEquals( "Error reading int attr",
                                 40,
                                 intAccessor.getIntValue( instance ) );
            Assert.assertEquals( "Error reading String attr",
                                 testString2,
                                 strAccessor.getValue( instance ) );

        } catch ( Exception e ) {
            e.printStackTrace();
            Assert.fail( "No exception is supposed to be generated when creating field accessor: " + e );
        }
    }

    public void testNullOnPrimitives() {
        try {
            ClassFieldAccessor intAccessor = cache.getAccessor( TestClass.class,
                                                                "intAttr",
                                                                getClass().getClassLoader() );
            ClassFieldAccessor strAccessor = cache.getAccessor( TestClass.class,
                                                                "strAttr",
                                                                getClass().getClassLoader() );
            ClassFieldAccessor byteAccessor = cache.getAccessor( TestClass.class,
                                                                 "byteAttr",
                                                                 getClass().getClassLoader() );
            ClassFieldAccessor booleanAccessor = cache.getAccessor( TestClass.class,
                                                                    "booleanAttr",
                                                                    getClass().getClassLoader() );
            ClassFieldAccessor charAccessor = cache.getAccessor( TestClass.class,
                                                                 "charAttr",
                                                                 getClass().getClassLoader() );
            ClassFieldAccessor doubleAccessor = cache.getAccessor( TestClass.class,
                                                                   "doubleAttr",
                                                                   getClass().getClassLoader() );
            ClassFieldAccessor floatAccessor = cache.getAccessor( TestClass.class,
                                                                  "floatAttr",
                                                                  getClass().getClassLoader() );
            ClassFieldAccessor longAccessor = cache.getAccessor( TestClass.class,
                                                                 "longAttr",
                                                                 getClass().getClassLoader() );
            ClassFieldAccessor shortAccessor = cache.getAccessor( TestClass.class,
                                                                  "shortAttr",
                                                                  getClass().getClassLoader() );

            String testString1 = "TestAttr1";
            TestClass instance = new TestClass();
            instance.setIntAttr( 25 );
            instance.setStrAttr( testString1 );
            instance.setByteAttr( (byte) 10 );
            instance.setBooleanAttr( true );
            instance.setCharAttr( 'x' );
            instance.setDoubleAttr( 10.5d );
            instance.setFloatAttr( 40.3f );
            instance.setLongAttr( 43l );
            instance.setShortAttr( (short) 20 );

            intAccessor.setValue( instance,
                                  null );
            strAccessor.setValue( instance,
                                  null );
            byteAccessor.setValue( instance,
                                   null );
            booleanAccessor.setValue( instance,
                                      null );
            charAccessor.setValue( instance,
                                   null );
            doubleAccessor.setValue( instance,
                                     null );
            floatAccessor.setValue( instance,
                                    null );
            longAccessor.setValue( instance,
                                   null );
            shortAccessor.setValue( instance,
                                    null );

            Assert.assertEquals( "Error setting attr",
                                 0,
                                 instance.getIntAttr() );
            Assert.assertNull( "Error setting attr",
                               instance.getStrAttr() );
            Assert.assertEquals( "Error setting attr",
                                 0,
                                 instance.getByteAttr() );
            Assert.assertEquals( "Error setting attr",
                                 false,
                                 instance.isBooleanAttr() );
            Assert.assertEquals( "Error setting attr",
                                 '\0',
                                 instance.getCharAttr() );
            Assert.assertEquals( "Error setting attr",
                                 0.0d,
                                 instance.getDoubleAttr(),
                                 0.1d );
            Assert.assertEquals( "Error setting attr",
                                 0.0f,
                                 instance.getFloatAttr(),
                                 0.1f );
            Assert.assertEquals( "Error setting attr",
                                 0l,
                                 instance.getLongAttr() );
            Assert.assertEquals( "Error setting attr",
                                 (short) 0,
                                 instance.getShortAttr() );

            Assert.assertEquals( "Error reading int attr",
                                 0,
                                 ((Integer) intAccessor.getValue( instance )).intValue() );
            Assert.assertNull( "Error reading String attr",
                               strAccessor.getValue( instance ) );
            Assert.assertEquals( "Error reading attr",
                                 0,
                                 ((Byte) byteAccessor.getValue( instance )).byteValue() );
            Assert.assertEquals( "Error reading attr",
                                 false,
                                 ((Boolean) booleanAccessor.getValue( instance )).booleanValue() );
            Assert.assertEquals( "Error reading attr",
                                 '\0',
                                 ((Character) charAccessor.getValue( instance )).charValue() );
            Assert.assertEquals( "Error reading attr",
                                 0.0d,
                                 ((Double) doubleAccessor.getValue( instance )).doubleValue(),
                                 0.1d );
            Assert.assertEquals( "Error reading attr",
                                 0.0f,
                                 ((Float) floatAccessor.getValue( instance )).floatValue(),
                                 0.1f );
            Assert.assertEquals( "Error reading attr",
                                 0l,
                                 ((Long) longAccessor.getValue( instance )).longValue() );
            Assert.assertEquals( "Error reading attr",
                                 (short) 0,
                                 ((Short) shortAccessor.getValue( instance )).shortValue() );

        } catch ( Exception e ) {
            e.printStackTrace();
            Assert.fail( "No exception is supposed to be generated when creating field accessor: " + e );
        }
    }

    public static class TestClass {
        private int     intAttr;
        private String  strAttr;
        private byte    byteAttr;
        private boolean booleanAttr;
        private char    charAttr;
        private double  doubleAttr;
        private float   floatAttr;
        private long    longAttr;
        private short   shortAttr;

        /**
         * @return Returns the intAttr.
         */
        public int getIntAttr() {
            return intAttr;
        }

        /**
         * @param intAttr The intAttr to set.
         */
        public void setIntAttr(int intAttr) {
            this.intAttr = intAttr;
        }

        /**
         * @return Returns the strAttr.
         */
        public String getStrAttr() {
            return strAttr;
        }

        /**
         * @param strAttr The strAttr to set.
         */
        public void setStrAttr(String strAttr) {
            this.strAttr = strAttr;
        }

        /**
         * @return Returns the booleanAttr.
         */
        public boolean isBooleanAttr() {
            return booleanAttr;
        }

        /**
         * @param booleanAttr The booleanAttr to set.
         */
        public void setBooleanAttr(boolean booleanAttr) {
            this.booleanAttr = booleanAttr;
        }

        /**
         * @return Returns the byteAttr.
         */
        public byte getByteAttr() {
            return byteAttr;
        }

        /**
         * @param byteAttr The byteAttr to set.
         */
        public void setByteAttr(byte byteAttr) {
            this.byteAttr = byteAttr;
        }

        /**
         * @return Returns the charAttr.
         */
        public char getCharAttr() {
            return charAttr;
        }

        /**
         * @param charAttr The charAttr to set.
         */
        public void setCharAttr(char charAttr) {
            this.charAttr = charAttr;
        }

        /**
         * @return Returns the doubleAttr.
         */
        public double getDoubleAttr() {
            return doubleAttr;
        }

        /**
         * @param doubleAttr The doubleAttr to set.
         */
        public void setDoubleAttr(double doubleAttr) {
            this.doubleAttr = doubleAttr;
        }

        /**
         * @return Returns the floatAttr.
         */
        public float getFloatAttr() {
            return floatAttr;
        }

        /**
         * @param floatAttr The floatAttr to set.
         */
        public void setFloatAttr(float floatAttr) {
            this.floatAttr = floatAttr;
        }

        /**
         * @return Returns the longAttr.
         */
        public long getLongAttr() {
            return longAttr;
        }

        /**
         * @param longAttr The longAttr to set.
         */
        public void setLongAttr(long longAttr) {
            this.longAttr = longAttr;
        }

        /**
         * @return Returns the shortAttr.
         */
        public short getShortAttr() {
            return shortAttr;
        }

        /**
         * @param shortAttr The shortAttr to set.
         */
        public void setShortAttr(short shortAttr) {
            this.shortAttr = shortAttr;
        }
    }

}