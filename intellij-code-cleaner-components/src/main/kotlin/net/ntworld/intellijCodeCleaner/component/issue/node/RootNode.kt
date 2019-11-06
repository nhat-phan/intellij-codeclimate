package net.ntworld.intellijCodeCleaner.component.issue.node

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.openapi.project.Project as IdeaProject
import com.intellij.ui.SimpleTextAttributes
import net.ntworld.intellijCodeCleaner.data.IssueNode

class RootNode(
    ideaProject: IdeaProject,
    private val data: IssueNode
) : NodeBase(ideaProject, data) {

    override fun createPresentation(): PresentationData {
        val presentation = PresentationData()
        presentation.setIcon(AllIcons.Modules.SourceRoot)
        presentation.addText(data.name, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)

        return presentation
    }

}