package cn.edu.fudan.se.helpseeking.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;

public class ContextUtil {
	private static HashMap<String, String> sources = new HashMap<String, String>();

	private static String handleIdentifier = "";

	private static String lineno = "";

	private static String method = "";

	private static String type = "";

	private static String file = "";

	private static String packages = "";

	private static String project = "";

	public static String getHandleIdentifier() {
		return handleIdentifier;
	}

	public static void setHandleIdentifier(String handleIdentifier) {
		ContextUtil.handleIdentifier = handleIdentifier;
	}

	public static String getLineno() {
		return lineno;
	}

	public static void setLineno(String lineno) {
		ContextUtil.lineno = lineno;
	}

	public static String getMethod() {
		return method;
	}

	public static void setMethod(String method) {
		ContextUtil.method = method;
	}

	public static String getType() {
		return type;
	}

	public static void setType(String type) {
		ContextUtil.type = type;
	}

	public static String getFile() {
		return file;
	}

	public static void setFile(String file) {
		ContextUtil.file = file;
	}

	public static String getPackages() {
		return packages;
	}

	public static void setPackages(String packages) {
		ContextUtil.packages = packages;
	}

	public static String getProject() {
		return project;
	}

	public static void setProject(String project) {
		ContextUtil.project = project;
	}

	public static void setContext(IJavaElement element) {
		String handle = element.getHandleIdentifier();
		ContextUtil.setHandleIdentifier(handle);
		int h = handle.indexOf('=');
		if (h > -1) {
			handle = handle.substring(h + 1);
			int i = handle.indexOf('/');
			if (i > -1) {
				ContextUtil.setProject(handle.substring(0, i));
				handle = handle.substring(i + 1);
				int j = handle.indexOf('{');
				if (j > -1) {
					ContextUtil.setPackages(handle.substring(0, j));
					handle = handle.substring(j + 1);
					int k = handle.indexOf('[');
					if (k > -1) {
						ContextUtil.setFile(handle.substring(0, k));
						handle = handle.substring(k + 1);
						int m = handle.indexOf('~');
						if (m > -1) {
							ContextUtil.setType(handle.substring(0, m));
							handle = handle.substring(m + 1);
							int n = handle.indexOf('~');
							if (n > -1) {
								ContextUtil.setMethod(handle.substring(0, n));
							} else {
								String method = handle.substring(0);
								if (method.equals("")) {
									ContextUtil.setMethod(null);
								} else {
									ContextUtil.setMethod(method);
								}
							}
						} else if (handle.length() > 0) {
							int o = handle.indexOf('^');
							if (o > -1) {
								handle = handle.substring(0, o);
							}
							ContextUtil.setType(handle);
							ContextUtil.setMethod(null);
							return;
						} else {
							ContextUtil.setType(null);
							ContextUtil.setMethod(null);
						}
					} else if (handle.length() > 0) {
						ContextUtil.setFile(handle);
						ContextUtil.setType(null);
						ContextUtil.setMethod(null);
					} else {
						ContextUtil.setFile(null);
						ContextUtil.setType(null);
						ContextUtil.setMethod(null);
					}
				} else {
					ContextUtil.setPackages(null);
				}
			}
		}
	}

	public static void addEditor(CompilationUnitEditor editor) {
		if (editor != null) {
			ITypeRoot typeRoot = JavaUI.getEditorInputTypeRoot(editor
					.getEditorInput());
			ICompilationUnit icu = (ICompilationUnit) typeRoot
					.getAdapter(ICompilationUnit.class);
			try {
				String source = icu.getSource();
				sources.put(icu.getHandleIdentifier(), source);
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void deleteEditor(final CompilationUnitEditor editor) {
		ITypeRoot typeRoot = JavaUI.getEditorInputTypeRoot(editor
				.getEditorInput());
		final ICompilationUnit icu = (ICompilationUnit) typeRoot
				.getAdapter(ICompilationUnit.class);
		if (editor != null
				&& sources.keySet().contains(icu.getHandleIdentifier())) {
			System.out.println("Origin: "
					+ sources.get(icu.getHandleIdentifier()));
			try {
				System.out.println("Now: " + icu.getSource());

				CompareConfiguration config = new CompareConfiguration();
				CompareEditorInput editorInput = new CompareEditorInput(config) {
					CompareItem left = new CompareItem(editor.getTitle()
							+ " Original Version", sources.get(icu
							.getHandleIdentifier()));
					CompareItem right = new CompareItem(editor.getTitle()
							+ " Original Version", icu.getSource());

					@Override
					protected Object prepareInput(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						return new DiffNode(null, Differencer.CHANGE, null,
								left, right);
					}
				};
				CompareUI.openCompareDialog(editorInput);
				sources.remove(icu.getHandleIdentifier());
			} catch (JavaModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class CompareItem extends BufferedContent implements ITypedElement,
		IModificationDate, IEditableContent {

	private String filename;
	private String content;

	CompareItem(String filename, String content) {
		this.filename = filename;
		this.content = content;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public ITypedElement replace(ITypedElement dest, ITypedElement src) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getModificationDate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected InputStream createStream() throws CoreException {
		return new ByteArrayInputStream(content.getBytes());
	}

	@Override
	public String getName() {
		return filename;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
