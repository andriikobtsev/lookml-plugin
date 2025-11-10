package com.yourcompany.lookml.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import com.yourcompany.lookml.LookMLFileType
import com.yourcompany.lookml.LookMLLanguage

class LookMLFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LookMLLanguage) {
    override fun getFileType() = LookMLFileType
    override fun toString() = "LookML File"
}
