/*
 * Copyright (c) 2003-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.validation.tutorial.util;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.validation.formatter.RelativeDateFormatter;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.DateFormat;

/**
 * Consists only of static methods that vend formatted text fields used
 * to edit dates that are bound to an underlying ValueModel.
 * Extends the Binding library's BasicComponentFactory to inherit
 * all factory methods from that class.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.10 $
 * @see com.jgoodies.binding.adapter.BasicComponentFactory
 * @see com.jgoodies.binding.adapter.Bindings
 */
public final class ExampleComponentFactory extends BasicComponentFactory {


  private ExampleComponentFactory() {
    // Suppresses default constructor, ensuring non-instantiability.
  }

  /**
   * Creates and returns a formatted text field that is bound
   * to the Date value of the given <code>ValueModel</code>.<p>
   * <p/>
   * The JFormattedTextField is configured with an AbstractFormatter
   * that uses two different DateFormats to edit and display the Date.
   * A <code>SHORT</code> DateFormat with strict checking is used to edit
   * (parse) a date; the DateFormatter's default DateFormat is used to
   * display (format) a date. In both cases <code>null</code> Dates are
   * mapped to the empty String.<p>
   * <p/>
   * In addition to formatted Dates, the parser accepts positive and
   * negative integers and interprets them as Dates relative to today.
   * For example -1 is yesterday, 1 is tomorrow, and 7 is "in a week".<p>
   * <p/>
   * Yesterday, today, and tomorrow are displayed as these Strings,
   * not as formatted Dates.
   *
   * @param valueModel the model that holds the value to be edited
   * @return a formatted text field for Date instances that is bound
   * @throws NullPointerException if the model is <code>null</code>
   */
  public static JFormattedTextField createDateField(
      ValueModel valueModel) {
    return createDateField(valueModel, true);
  }


  /**
   * Creates and returns a formatted text field that is bound
   * to the Date value of the given <code>ValueModel</code>.<p>
   * <p/>
   * The JFormattedTextField is configured with an AbstractFormatter
   * that uses two different DateFormats to edit and display the Date.
   * A <code>SHORT</code> DateFormat with strict checking is used to edit
   * (parse) a date; the DateFormatter's default DateFormat is used to
   * display (format) a date. In both cases <code>null</code> Dates are
   * mapped to the empty String.<p>
   * <p/>
   * In addition to formatted Dates, the parser accepts positive and
   * negative integers and interprets them as Dates relative to today.
   * For example -1 is yesterday, 1 is tomorrow, and 7 is "in a week".<p>
   * <p/>
   * If <code>enableShortcuts</code> is set to <code>true</code>,
   * yesterday, today, and tomorrow are displayed as these Strings,
   * not as formatted Dates.
   *
   * @param valueModel      the model that holds the value to be edited
   * @param enableShortcuts true to display yesterday, today, and tomorrow
   *                        with natural language strings
   * @return a formatted text field for Date instances that is bound
   * @throws NullPointerException if the model is <code>null</code>
   */
  public static JFormattedTextField createDateField(
      ValueModel valueModel,
      boolean enableShortcuts) {
    return createDateField(valueModel, enableShortcuts, false);
  }


  /**
   * Creates and returns a formatted text field that is bound
   * to the Date value of the given <code>ValueModel</code>.<p>
   * <p/>
   * The JFormattedTextField is configured with an AbstractFormatter
   * that uses two different DateFormats to edit and display the Date.
   * A <code>SHORT</code> DateFormat with strict checking is used to edit
   * (parse) a date; the DateFormatter's default DateFormat is used to
   * display (format) a date. In both cases <code>null</code> Dates are
   * mapped to the empty String.<p>
   * <p/>
   * In addition to formatted Dates, the parser accepts positive and
   * negative integers and interprets them as Dates relative to today.
   * For example -1 is yesterday, 1 is tomorrow, and 7 is "in a week".<p>
   * <p/>
   * If <code>enableShortcuts</code> is set to <code>true</code>,
   * yesterday, today, and tomorrow are displayed as these Strings,
   * not as formatted Dates.
   *
   * @param valueModel         the model that holds the value to be edited
   * @param enableShortcuts    true to display yesterday, today, and tomorrow
   *                           with natural language strings
   * @param commitsOnValidEdit true to commit on valid edit,
   *                           false to commit on focus lost
   * @return a formatted text field for Date instances that is bound
   * @throws NullPointerException if the model is <code>null</code>
   */
  public static JFormattedTextField createDateField(
      ValueModel valueModel,
      boolean enableShortcuts,
      boolean commitsOnValidEdit) {
    DateFormat shortFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    shortFormat.setLenient(false);

    DefaultFormatter defaultFormatter =
        new RelativeDateFormatter(shortFormat, false, true);
    defaultFormatter.setCommitsOnValidEdit(commitsOnValidEdit);

    JFormattedTextField.AbstractFormatter displayFormatter =
        new RelativeDateFormatter(enableShortcuts, true);

    DefaultFormatterFactory formatterFactory =
        new DefaultFormatterFactory(defaultFormatter, displayFormatter);

    JFormattedTextField textField = new JFormattedTextField(formatterFactory);
    Bindings.bind(textField, valueModel);
    return textField;
  }


}