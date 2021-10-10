package com.xin.portal.system.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xin.portal.system.bean.TreeNode;

public class TreeUtil {

	@SuppressWarnings("unchecked")
	private List<TreeNode> buildListToTree(List<TreeNode> dirs) {
		List<TreeNode> roots = findRoots(dirs);
		List<TreeNode> notRoots = (List<TreeNode>) CollectionUtils.subtract(dirs, roots);
		for (TreeNode root : roots) {
			root.setChildren(findChildren(root, notRoots));
		}
		return roots;
	}

	public List<TreeNode> findRoots(List<TreeNode> allNodes) {
		List<TreeNode> results = new ArrayList<TreeNode>();
		for (TreeNode node : allNodes) {
			boolean isRoot = true;
			for (TreeNode comparedOne : allNodes) {
				if (node.getParentId() == comparedOne.getId()) {
					isRoot = false;
					break;
				}
			}
			if (isRoot) {
				node.setLevel(0);
				results.add(node);
				node.setRootId(node.getId());
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private List<TreeNode> findChildren(TreeNode root, List<TreeNode> allNodes) {
		List<TreeNode> children = new ArrayList<TreeNode>();

		for (TreeNode comparedOne : allNodes) {
			if (comparedOne.getParentId() == root.getId()) {
				comparedOne.setParentId(root.getId());
				comparedOne.setLevel(root.getLevel() + 1);
				children.add(comparedOne);
			}
		}
		List<TreeNode> notChildren = (List<TreeNode>) CollectionUtils.subtract(allNodes, children);
		for (TreeNode child : children) {
			List<TreeNode> tmpChildren = findChildren(child, notChildren);
			if (tmpChildren == null || tmpChildren.size() < 1) {
				child.setLeaf(true);
			} else {
				child.setLeaf(false);
			}
			child.setChildren(tmpChildren);
		}
		return children;
	}

   

}
