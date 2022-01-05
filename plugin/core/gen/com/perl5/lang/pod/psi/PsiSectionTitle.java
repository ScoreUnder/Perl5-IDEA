// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;

public interface PsiSectionTitle extends PodSectionTitle {

  @NotNull
  List<PsiPodFormatBold> getPodFormatBoldList();

  @NotNull
  List<PsiPodFormatCode> getPodFormatCodeList();

  @NotNull
  List<PsiPodFormatEscape> getPodFormatEscapeList();

  @NotNull
  List<PsiPodFormatFile> getPodFormatFileList();

  @NotNull
  List<PsiPodFormatIndex> getPodFormatIndexList();

  @NotNull
  List<PsiPodFormatItalic> getPodFormatItalicList();

  @NotNull
  List<PsiPodFormatLink> getPodFormatLinkList();

  @NotNull
  List<PsiPodFormatNbsp> getPodFormatNbspList();

  @NotNull
  List<PsiPodFormatNull> getPodFormatNullList();

}
