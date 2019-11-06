package net.ntworld.intellijCodeCleaner.component.issue

import com.intellij.ide.util.treeView.NodeRenderer
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import net.ntworld.codeCleaner.structure.Issue
import net.ntworld.intellijCodeCleaner.*
import net.ntworld.intellijCodeCleaner.component.issue.node.DirectoryNode
import net.ntworld.intellijCodeCleaner.component.issue.node.FileNode
import net.ntworld.intellijCodeCleaner.component.issue.node.NodeBase
import net.ntworld.intellijCodeCleaner.component.issue.node.RootNode
import net.ntworld.intellijCodeCleaner.data.IssueNode
import net.ntworld.intellijCodeCleaner.data.IssueNodeBuilder
import net.ntworld.intellijCodeCleaner.state.ProjectState
import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeCellRenderer

open class IssueTree(
    private val ideaProject: Project,
    data: List<Issue>
) : TreeCellRenderer {
    private val tree = Tree()
    private val renderer = NodeRenderer()

    val component: Tree = tree
    private val model = DefaultTreeModel(makeRootTreeNode(data))

    init {
        tree.model = model
        tree.cellRenderer = this
    }

    fun updateBy(data: Collection<Issue>) {
        model.setRoot(makeRootTreeNode(data))
    }

    protected open fun makeRootTreeNode(data: Collection<Issue>): DefaultMutableTreeNode {
        if (data.isEmpty()) {
            return DefaultMutableTreeNode()
        }

        val builder = IssueNodeBuilder()
        data.forEach { builder.add(it) }

        return buildDefaultMutableTreeNode(builder.build())
    }

    protected open fun buildDefaultMutableTreeNode(nodeData: IssueNode): DefaultMutableTreeNode {
        val node = DefaultMutableTreeNode(makeNode(nodeData))
        for (childNodeData in nodeData.children) {
            node.add(buildDefaultMutableTreeNode(childNodeData))
        }
        return node
    }

    protected open fun makeNode(nodeData: IssueNode): NodeBase {
        return when (nodeData.type) {
            ISSUE_NODE_TYPE_ROOT -> RootNode(ideaProject, nodeData)
            ISSUE_NODE_TYPE_DIRECTORY -> DirectoryNode(ideaProject, nodeData)
            ISSUE_NODE_TYPE_FILE -> FileNode(ideaProject, nodeData)
            ISSUE_NODE_TYPE_ISSUE, ISSUE_NODE_TYPE_RELATED_ISSUE -> {
                net.ntworld.intellijCodeCleaner.component.issue.node.IssueNode(
                    ideaProject,
                    nodeData
                )
            }
            else -> throw Exception("Node $nodeData is not supported")
        }
    }

    override fun getTreeCellRendererComponent(
        tree: JTree?,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        return renderer.getTreeCellRendererComponent(
            tree,
            value,
            selected,
            expanded,
            leaf,
            row,
            hasFocus
        )
    }
}