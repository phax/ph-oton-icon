/*
 * Copyright (C) 2026 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.photon.icon.fontawesome6.tools.supplementary;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

import org.jspecify.annotations.NonNull;

import com.helger.base.string.StringReplace;
import com.helger.collection.commons.CommonsTreeSet;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.commons.ICommonsSet;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSSelectorSimpleMember;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.io.resource.ClassPathResource;
import com.helger.json.IJson;
import com.helger.json.IJsonArray;
import com.helger.json.IJsonObject;
import com.helger.json.serialize.JsonReader;
import com.helger.photon.icon.fontawesome6.EIconCSSPathProvider;

/**
 * Regenerates {@link com.helger.photon.icon.fontawesome6.CFontAwesome6CSS} and
 * {@link com.helger.photon.icon.fontawesome6.EFontAwesome6Icon} from the bundled FontAwesome 6
 * resources.
 * <p>
 * Unlike FontAwesome 5, FontAwesome 6 no longer defines a <code>.fa-name:before</code> pseudo-rule
 * per icon &ndash; the icon glyph is a <code>--fa</code> CSS custom property on a plain
 * <code>.fa-name</code> class, and the style (solid/regular/brands) is a separate class. Therefore
 * the list of icons and their available free styles is taken from the official
 * <code>metadata/icons.json</code>, while <code>all.css</code> is only parsed to emit the raw
 * {@link com.helger.html.css.ICSSClassProvider} constants.
 *
 * @author Philip Helger
 */
public class MainExtractFontAwesome6CSSClasses
{
  @NonNull
  static String createFieldName (@NonNull final String s)
  {
    String sFieldName = s.toUpperCase (Locale.US);
    sFieldName = StringReplace.replaceAll (sFieldName, '-', '_');
    if (Character.isDigit (sFieldName.charAt (0)))
      sFieldName = "_" + sFieldName;
    return sFieldName;
  }

  public static void main (final String [] args)
  {
    // 1. All raw CSS classes from all.css -> CFontAwesome6CSS constants
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (new ClassPathResource (EIconCSSPathProvider.FONT_AWESOME6.getCSSItemPath (true)),
                                                               new CSSReaderSettings ().setFallbackCharset (StandardCharsets.UTF_8));
    final ICommonsSet <String> aClasses = new CommonsTreeSet <> ();
    CSSVisitor.visitCSS (aCSS, new DefaultCSSVisitor ()
    {
      @Override
      public void onStyleRuleSelector (@NonNull final CSSSelector aSelector)
      {
        final ICommonsList <ICSSSelectorMember> aMembers = aSelector.getAllMembers ();
        for (final ICSSSelectorMember aMember : aMembers)
          if (aMember instanceof final CSSSelectorSimpleMember aSM)
          {
            if (aSM.isClass ())
              aClasses.add (aSM.getValue ());
          }
      }
    });

    for (final String sClass : aClasses)
    {
      final String sClassName = sClass.substring (1);
      final String sFieldName = createFieldName (sClassName);
      System.out.println ("public static final ICSSClassProvider " +
                          sFieldName +
                          " = DefaultCSSClassProvider.create (\"" +
                          sClassName +
                          "\");");
    }

    System.out.println ();

    // 2. All icons and their free styles from metadata/icons.json -> enum entries
    final IJsonObject aObject = JsonReader.builder ()
                                          .source (new ClassPathResource ("external/fontawesome/6.7.2/metadata/icons.json"))
                                          .readAsObject ();
    // TreeSet on the icon id to get a stable, sorted output
    final ICommonsSet <String> aIconIDs = new CommonsTreeSet <> ();
    for (final Map.Entry <String, IJson> aEntry : aObject)
      aIconIDs.add (aEntry.getKey ());

    for (final String sID : aIconIDs)
    {
      final IJsonArray aFree = aObject.getAsObject (sID).getAsArray ("free");
      // Precedence: brands (exclusive) > solid > regular
      final String sStyle;
      if (aFree.contains ("brands"))
        sStyle = "BRANDS";
      else
        if (aFree.contains ("solid"))
          sStyle = "SOLID";
        else
          sStyle = "REGULAR";

      final String sCSSFieldName = createFieldName ("fa-" + sID);
      final String sEnumName = createFieldName (sID);
      System.out.println (sEnumName +
                          " (CFontAwesome6CSS." +
                          sCSSFieldName +
                          ", EFontAwesome6IconStyle." +
                          sStyle +
                          "),");
    }
  }
}
