package net.ntworld.intellijCodeCleaner.component.issue

import com.intellij.find.FindModel
import com.intellij.find.impl.FindInProjectUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.OnePixelSplitter
import com.intellij.ui.ScrollPaneFactory
import com.intellij.usages.impl.UsagePreviewPanel
import net.ntworld.codeCleaner.structure.Issue
import net.ntworld.intellijCodeCleaner.AppStore
import net.ntworld.intellijCodeCleaner.ComponentFactory
import javax.swing.JPanel

abstract class AbstractIssueTab(
    private val ideaProject: Project,
    private val toolWindow: ToolWindow,
    private val componentFactory: ComponentFactory
) {
    protected abstract val dividerKey: String

    protected abstract fun getIssues(store: AppStore): Collection<Issue>

    private val splitter by lazy {
        OnePixelSplitter(false, dividerKey, 0.5f)
    }
    protected open val issueTree = IssueTree(ideaProject, listOf())

    fun createPanel(): JPanel {
        val store = componentFactory.makeDispatcher().store
        val usagePreviewPanel = UsagePreviewPanel(
            ideaProject,
            FindInProjectUtil.setupViewPresentation(false, FindModel())
        )
        store.onChange("project", this::updateComponents)

        splitter.firstComponent = ScrollPaneFactory.createScrollPane(issueTree.component)
        splitter.secondComponent = ScrollPaneFactory.createScrollPane(usagePreviewPanel)

        return splitter
    }

    protected open fun updateComponents() {
        val store = componentFactory.makeDispatcher().store
        if (!store.project.hasResult) {
            issueTree.updateBy(listOf())
        } else {
            issueTree.updateBy(getIssues(store))
        }
    }
}