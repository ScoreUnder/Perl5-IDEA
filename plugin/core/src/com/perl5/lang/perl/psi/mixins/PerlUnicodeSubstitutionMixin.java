/*
 * Copyright 2015-2020 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.NUMBER_HEX;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.STRING_CHAR_NAME;

public class PerlUnicodeSubstitutionMixin extends PerlNumericCharSubstitution {
  public PerlUnicodeSubstitutionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public int getCodePoint() {
    PsiElement charCodeElement = findChildByType(NUMBER_HEX);
    return charCodeElement == null ? getCharCodeFromName() : Integer.parseInt(charCodeElement.getText(), 16);
  }

  private int getCharCodeFromName() {
    PsiElement charNameElement = findChildByType(STRING_CHAR_NAME);
    if (charNameElement != null) {
      try {
        return Character.codePointOf(charNameElement.getText());
      }
      catch (IllegalArgumentException ignore) {
      }
    }
    return -1;
  }
}