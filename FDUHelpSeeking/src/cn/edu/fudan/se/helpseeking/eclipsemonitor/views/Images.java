package cn.edu.fudan.se.helpseeking.eclipsemonitor.views;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.osgi.framework.Bundle;

public class Images {

	private static final URL baseUrl;

	static {
		Bundle bundle = Platform.getBundle("cn.edu.fudan.se.helpseeking.Eclipsemonitor");
		if (bundle != null) {
			baseUrl = bundle.getEntry("/icons/");
		} else {
			URL iconsUrl = null;
			try {
				iconsUrl = new URL(Images.class.getResource("Images.class"),
						"../../../../../../../icons/");
				//TODO:  package URL like  cn.edu.fudan.se.helpseeking.eclipsemonitor.views.Images.java ==> "icons" directory can locate from this file position  
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			baseUrl = iconsUrl;
		}
	}

	private static ImageRegistry imageRegistry;

	public static final ImageDescriptor ABSTRACT = create("abstract.gif");
	public static final ImageDescriptor ADD_ACTIVATE = create("add_activate.gif");
	public static final ImageDescriptor ADD_DEACTIVATE = create("add_deactivate.gif");
	public static final ImageDescriptor CLASS_DEFAULT = create("class_default.gif");
	public static final ImageDescriptor CLASS = create("class.gif");
	public static final ImageDescriptor ENUM = create("enum.gif");
	public static final ImageDescriptor FIELD_DEFAULT_OBJ = create("field_default_obj.gif");
	public static final ImageDescriptor FIELD_PRIVATE_OBJ = create("field_private_obj.gif");
	public static final ImageDescriptor FIELD_PROTECTED_OBJ = create("field_protected_obj.gif");
	public static final ImageDescriptor FIELD_PUBLIC_OBJ = create("field_public_obj.gif");
	public static final ImageDescriptor FINAL_CO = create("final_co.gif");
	public static final ImageDescriptor METHDEF_OBJ = create("methdef_obj.gif");
	public static final ImageDescriptor METHPRI_OBJ = create("methpri_obj.gif");
	public static final ImageDescriptor METHPRO_OBJ = create("methpro_obj.gif");
	public static final ImageDescriptor METHPUB_OBJ = create("methpub_obj.gif");
	public static final ImageDescriptor NATIVE_CO = create("native_co.gif");
	public static final ImageDescriptor PACKAGE_OBJ = create("package_obj.gif");
	public static final ImageDescriptor PACKAGE = create("package.gif");
	public static final ImageDescriptor PACKAGES = create("packages.gif");
	public static final ImageDescriptor REFRESH = create("refresh.gif");
	public static final ImageDescriptor SEQ = create("seq.gif");
	public static final ImageDescriptor START_ACTIVATE = create("start_activate.gif");
	public static final ImageDescriptor START_DEACTIVATE = create("start_deactivate.gif");
	public static final ImageDescriptor STOP_ACTIVATE = create("stop_activate.gif");
	public static final ImageDescriptor STOP_DEACTIVATE = create("stop_deactivate.gif");
	public static final ImageDescriptor TYPES = create("types.gif");
	public static final ImageDescriptor VOLATILE_CO = create("volatile.gif");
	public static final ImageDescriptor PREVIOUS = create("previous.gif");
	public static final ImageDescriptor NEXT = create("next.gif");

	private static ImageDescriptor create(String name) {
		try {
			return ImageDescriptor.createFromURL(makeIconFileURL(name));
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}

	private static URL makeIconFileURL(String name)
			throws MalformedURLException {
		if (baseUrl == null) {
			throw new MalformedURLException();
		}
		StringBuffer buffer = new StringBuffer(name);
		return new URL(baseUrl, buffer.toString());
	}
}
