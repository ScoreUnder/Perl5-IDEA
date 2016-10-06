/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.moose.psi.references.resolvers;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAugmentStatement;
import com.perl5.lang.perl.parser.moose.psi.references.PerlMooseInnerReference;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementStub;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 25.01.2016.
 */
public class PerlMooseInnerReferenceResolver implements ResolveCache.PolyVariantResolver<PerlMooseInnerReference>
{
	@NotNull
	@Override
	public ResolveResult[] resolve(@NotNull PerlMooseInnerReference reference, boolean incompleteCode)
	{
		List<ResolveResult> result = new ArrayList<ResolveResult>();
		PsiElement element = reference.getElement();

		String subName = null;
		PerlSubDefinitionBase subDefinitionBase = PsiTreeUtil.getParentOfType(element, PerlSubDefinitionBase.class);

		if (subDefinitionBase != null)
		{
			subName = subDefinitionBase.getSubName();
		}

		PerlMooseAugmentStatement augmentStatement = PsiTreeUtil.getParentOfType(element, PerlMooseAugmentStatement.class);

		if (augmentStatement != null)
		{
			subName = augmentStatement.getSubName();
		}

		if (subName != null)
		{
			PerlNamespaceDefinition namespaceDefinition = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinition.class);
			Set<PerlNamespaceDefinition> recursionSet = new THashSet<PerlNamespaceDefinition>();

			if (StringUtil.isNotEmpty(subName) && namespaceDefinition != null)
			{
				collectNamespaceMethodsAugmentations(namespaceDefinition, subName, recursionSet, result);
			}
		}

		return result.toArray(new ResolveResult[result.size()]);
	}

	protected void collectNamespaceMethodsAugmentations(@NotNull PerlNamespaceDefinition namespaceDefinition,
														@NotNull String subName,
														Set<PerlNamespaceDefinition> recursionSet,
														List<ResolveResult> result)
	{
		recursionSet.add(namespaceDefinition);

		for (PerlNamespaceDefinition childNamespace : namespaceDefinition.getChildNamespaceDefinitions())
		{
			if (!recursionSet.contains(childNamespace))
			{
				boolean noSubclasses = false;

				for (PsiElement augmentStatement : getAugmentStatements(childNamespace))
				{
					if (subName.equals(((PerlMooseAugmentStatement) augmentStatement).getSubName()))
					{
						result.add(new PsiElementResolveResult(augmentStatement));
						noSubclasses = true;
					}
				}

				if (!noSubclasses)
				{
					collectNamespaceMethodsAugmentations(childNamespace, subName, recursionSet, result);
				}
			}
		}
	}

	private static List<PsiElement> getAugmentStatements(@NotNull final PsiElement childNamespace)
	{
		return CachedValuesManager.getCachedValue(childNamespace, new CachedValueProvider<List<PsiElement>>()
		{
			@Nullable
			@Override
			public Result<List<PsiElement>> compute()
			{
				return Result.create(PerlPsiUtil.collectNamespaceMembers(childNamespace, PerlMooseAugmentStatementStub.class, PerlMooseAugmentStatement.class), childNamespace);
			}
		});
	}
}
