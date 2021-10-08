package com.github.n34t0.compose.stubs

import com.github.n34t0.ipw.CodeCompletionElement
import com.github.n34t0.ipw.GotoDeclarationData
import com.github.n34t0.ipw.IntellijPlatformWrapper
import com.github.n34t0.ipw.IpwProject
import com.github.n34t0.ipw.impl.GTDData

class CcwStub : IntellijPlatformWrapper {

    override fun init() {}

    override fun stop() {}

    override fun openProject(rootFolder: String): IpwProject = CcwProjectStub()

    override fun openFile(filePath: String): IpwProject = CcwProjectStub()

}

class CcwProjectStub : IpwProject {

    override fun addLibraries(p0: MutableList<String>) {}

    override fun synchronizeProjectDirectory() {}

    var count = 0
    var list = ArrayList<CodeCompletionElement>()

    override fun getCodeCompletion(p0: String, p1: Int): List<CodeCompletionElement> {
        repeat(3) {
            count++
            list.add(CcwElementStub("Sorted$count"))
        }
        return list
    }

    override fun gotoDeclaration(path: String?, caretOffset: Int): GotoDeclarationData =
        GTDData.NON_NAVIGATABLE

    override fun closeProject() {}

}

class CcwElementStub(
    private val name: String = "Name",
    private val type: String = "Type",
    private val tail: String = "Tail"
) : CodeCompletionElement {

    override fun getName(): String = name

    override fun getType(): String = type

    override fun getTail(): String = tail

    override fun toString(): String = "$type $name $tail"

}
