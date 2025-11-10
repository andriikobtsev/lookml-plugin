package com.yourcompany.lookml

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import com.yourcompany.lookml.psi.LookMLTypes

class LookMLQuoteHandler : SimpleTokenSetQuoteHandler(LookMLTypes.STRING)
