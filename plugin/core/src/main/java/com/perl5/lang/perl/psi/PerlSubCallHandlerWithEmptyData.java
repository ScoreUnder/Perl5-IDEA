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

package com.perl5.lang.perl.psi;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.stubs.calls.EmptyCallData;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class PerlSubCallHandlerWithEmptyData extends PerlSubCallHandler<EmptyCallData> {
  @Override
  public final void serialize(@NotNull PerlSubCallElementData callData, @NotNull StubOutputStream dataStream) throws IOException {
  }

  @Override
  public final EmptyCallData deserialize(@NotNull StubInputStream dataStream) {
    return EmptyCallData.INSTANCE;
  }

  @Override
  public final @NotNull EmptyCallData computeCallData(@NotNull PerlSubCallElement subCallElement) {
    return EmptyCallData.INSTANCE;
  }
}