/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.stubs.StubInputStream;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager.SCALAR_CONTEXT_ID;

public class PerlScalarContextValue extends PerlOperationValue {
  private static final Logger LOG = Logger.getInstance(PerlScalarContextValue.class);

  PerlScalarContextValue(@NotNull PerlValue baseValue) {
    super(baseValue);
    if (baseValue.getContextType() == PerlContextType.SCALAR) {
      LOG.error("Value is already a scalar: " + baseValue);
    }
    else if (baseValue.isDeterministic()) {
      LOG.error("Deterministic values should be resolved in place: " + baseValue);
    }
  }

  PerlScalarContextValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
  }

  @NotNull
  @Override
  protected PerlValue computeResolve(@NotNull PerlValue resolvedTarget, @NotNull PerlValueResolver resolver) {
    return resolvedTarget.isDeterministic() ? computeDeterministicResolve(resolvedTarget) : UNKNOWN_VALUE;
  }

  @NotNull
  private static PerlValue computeDeterministicResolve(@NotNull PerlValue resolvedTarget) {
    LOG.assertTrue(resolvedTarget.isDeterministic(), "Value should be deterministic");
    if (resolvedTarget.getContextType() == PerlContextType.SCALAR) {
      return resolvedTarget;
    }
    if (resolvedTarget instanceof PerlArrayValue) {
      return PerlScalarValue.create(((PerlArrayValue)resolvedTarget).getElements().size());
    }
    else if (resolvedTarget instanceof PerlHashValue) {
      return PerlScalarValue.create(((PerlHashValue)resolvedTarget).getMap().size());
    }
    return UNKNOWN_VALUE;
  }

  @Override
  protected int getSerializationId() {
    return SCALAR_CONTEXT_ID;
  }

  @Override
  public String toString() {
    return "scalar " + getBaseValue().toString();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return "scalar " + getBaseValue().getPresentableText();
  }

  @NotNull
  public static PerlValue create(@NotNull PerlValue baseValue) {
    if (baseValue.isUnknown()) {
      return UNKNOWN_VALUE;
    }
    if (baseValue.getContextType() == PerlContextType.SCALAR) {
      return baseValue;
    }
    return baseValue.isDeterministic() ? PerlValuesBuilder.convert(baseValue, PerlScalarContextValue::computeDeterministicResolve)
                                       : new PerlScalarContextValue(baseValue);
  }
}
