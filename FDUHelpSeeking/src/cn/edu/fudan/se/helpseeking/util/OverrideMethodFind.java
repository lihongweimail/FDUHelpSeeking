package cn.edu.fudan.se.helpseeking.util;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.edu.fudan.se.helpseeking.bean.OverrideMethods;

public class OverrideMethodFind {
	
	/* analyse Java project */
	public static void analyse(IJavaProject javaProject) throws CoreException {
	     /* iterate through every package inside Java project including  binary packages */
	     for (IPackageFragment packageFragment : javaProject.getPackageFragments())
	          /* analyse package fragment */ 
	          analyse(packageFragment);
	}
	 
	/* analyse package */
	public static void analyse(IPackageFragment packageFragment) throws CoreException {
	     /* check if package contains non-binary source file (.java not .class) */ 
	     if (packageFragment.getKind() != IPackageFragmentRoot.K_SOURCE)
	          return;
	 
	     /* iterate though all ".java" file within this package */ 
	     for (ICompilationUnit compilationUnit : packageFragment.getCompilationUnits()) {
	          /* make it temporary working copy (this won't change underlying file unless committed )*/
	          compilationUnit.becomeWorkingCopy(null);
	          /* analyse compilation unit */
	          analyse(compilationUnit);
	     }
	}
	 
	/* analyse compilation unit */
	public static void analyse(ICompilationUnit compilationUnit) throws CoreException {
	     /* parse and construct an AST from compilation unit */
	     ASTParser parser = ASTParser.newParser(AST.JLS4);
	     parser.setResolveBindings(true);
	     parser.setKind(ASTParser.K_COMPILATION_UNIT);
	     parser.setSource(compilationUnit);
	     CompilationUnit parsedAst = (CompilationUnit) parser.createAST(null);
	 
	     /* get all the class declaration (including inner classes) */
	     for(IType type : compilationUnit.getAllTypes())
	          analyse(type, parsedAst); /* this requires AST */
	}
	 
	/* analyse the class declaration for methods */
	public static void analyse(IType type, CompilationUnit parsedAst) throws CoreException {
	 
	     /* search the node for the class declaration */
	     ASTNode node = NodeFinder.perform(parsedAst, type.getSourceRange());
	     assert(node != null);
	     if(!(node instanceof TypeDeclaration))
	          return;
	 
	     /* this is AST node within compilation using corresponding to class (IType) */
	     TypeDeclaration typDec = (TypeDeclaration) node;
	 
	     /* find synchronised overridden methods */ 
	     findSynchOverridedMethods(typDec.resolveBinding());
	}
	 
	/* find synchronized overridden methods from type binding (class binding) */
	public static void findSynchOverridedMethods(ITypeBinding typeBind) {
	     /* get all method bindings from class binding */
	     IMethodBinding[] methodBinds = typeBind.getDeclaredMethods();
	     for(IMethodBinding methodBind : methodBinds) {
	          /* check if method binding is for synchronized method */
	         //everyone can check:   if(Modifier.isSynchronized(methodBind.getModifiers()))
	               /* search of similar method signature of super-class hierarchy */
	               findOverrideInHierarchy(methodBind, typeBind);
	     }
	}
	 
	/* find similar method signature in super class hierarchy */
	public static void findOverrideInHierarchy(IMethodBinding methodBind, ITypeBinding typeBind) {
	     /* get super class */
	     ITypeBinding superTypeBind = typeBind.getSuperclass();
	     if(superTypeBind == null)
	          return;
	 
	     IMethodBinding overridenMethodBind = null;
	     if((overridenMethodBind = matchMethodInType(methodBind, superTypeBind)) == null)
	          findOverrideInHierarchy(methodBind, superTypeBind);
	     else
	          System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
	 
	     /* search in super interfaces also */
	     ITypeBinding[] interfaceBinds = typeBind.getInterfaces();
	     for(ITypeBinding interfaceBind : interfaceBinds) {   
	          if((overridenMethodBind = matchMethodInType(methodBind, superTypeBind)) == null)
	               findOverrideInHierarchy(methodBind, superTypeBind);
	          else
	               System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
	     } 
	}
	 
	/* now matching the method signature with methods inside the type binding
	 * using erasure of type bind it should work for generic methods as well */ 
	public static IMethodBinding matchMethodInType(IMethodBinding methodBind, ITypeBinding typeBind) {
	 
	     /* iterate though all the methods within type bind */
	     for(IMethodBinding tMethodBind : typeBind.getDeclaredMethods()) {
	          if(!tMethodBind.getName().equals(methodBind.getName()))
	               continue;
	          if(tMethodBind.getParameterTypes().length != methodBind.getParameterTypes().length)
	               continue;
	 
	          ITypeBinding[] params1 = tMethodBind.getParameterTypes();
	          ITypeBinding[] param2 = methodBind.getParameterTypes();
	          
	          /* matching erasures instead of raw types */
	          for(int i = 0; i < params1.length; i++) {
	               if(params1[i].getErasure().equals(param2[i].getErasure()))
	                    continue;
	          }
	          return tMethodBind;
	     }
	     return null;
	}

	public static void analyse(ICompilationUnit compilationUnit,
			List<OverrideMethods> overrideMethodFinds) throws JavaModelException, CoreException {
	     /* parse and construct an AST from compilation unit */
	     ASTParser parser = ASTParser.newParser(AST.JLS4);
	     parser.setResolveBindings(true);
	     parser.setKind(ASTParser.K_COMPILATION_UNIT);
	     parser.setSource(compilationUnit);
	     CompilationUnit parsedAst = (CompilationUnit) parser.createAST(null);
	 
	     /* get all the class declaration (including inner classes) */
	     for(IType type : compilationUnit.getAllTypes())
	          analyse(type, parsedAst,overrideMethodFinds); /* this requires AST */
	
		
	}

	private static void analyse(IType type, CompilationUnit parsedAst,
			List<OverrideMethods> overrideMethodFinds) throws JavaModelException {
	     /* search the node for the class declaration */
	     ASTNode node = NodeFinder.perform(parsedAst, type.getSourceRange());
	     assert(node != null);
	     if(!(node instanceof TypeDeclaration))
	          return;
	 
	     /* this is AST node within compilation using corresponding to class (IType) */
	     TypeDeclaration typDec = (TypeDeclaration) node;
	 
	     /* find synchronised overridden methods */ 
	     findSynchOverridedMethods(typDec.resolveBinding(),overrideMethodFinds);
		
	}

	private static void findSynchOverridedMethods(ITypeBinding typeBind,
			List<OverrideMethods> overrideMethodFinds) {
	    /* get all method bindings from class binding */
	     IMethodBinding[] methodBinds = typeBind.getDeclaredMethods();
	     for(IMethodBinding methodBind : methodBinds) {
	          /* check if method binding is for synchronized method */
	         //everyone can check:   if(Modifier.isSynchronized(methodBind.getModifiers()))
	               /* search of similar method signature of super-class hierarchy */
	               findOverrideInHierarchy(methodBind, typeBind,overrideMethodFinds);
	     }		
	}

	private static void findOverrideInHierarchy(IMethodBinding methodBind,
			ITypeBinding typeBind, List<OverrideMethods> overrideMethodFinds) {
	     /* get super class */
	     ITypeBinding superTypeBind = typeBind.getSuperclass();
	     if(superTypeBind == null)
	          return;
	 
	     IMethodBinding overridenMethodBind = null;
	     if((overridenMethodBind = matchMethodInType(methodBind, superTypeBind)) == null)
	          findOverrideInHierarchy(methodBind, superTypeBind,overrideMethodFinds);
	     else
	     {  //qianmian 
       	  OverrideMethods oms=new OverrideMethods();
  	     
    	     //overridenMethodBind.getKey() :  Lorg/eclipse/ui/part/EditorPart;.doSave(Lorg/eclipse/core/runtime/IProgressMonitor;)V
    	          String className=overridenMethodBind.getKey().substring(0,(overridenMethodBind.getKey()).indexOf(";"));
    	                 if (className.contains("/")) {
    	                	 className=className.substring(className.lastIndexOf("/")+1);
    					}
    	                 String methodName=overridenMethodBind.getName();
    	                 
    	                 
    	          String methodNamestr=overridenMethodBind.getKey().substring((overridenMethodBind.getKey()).lastIndexOf(".")+1);
    	                 methodNamestr=methodNamestr.substring(0, methodNamestr.length()-1);
    	                 
    	         
    	        		  	          
    	          String currentMethodName=methodName+"(";
    	                 
    	          List<String> methodnamelist=CommUtil.arrayToList(methodNamestr.split("[;]"));
    	          for (int i = 0; i < methodnamelist.size(); i++) {
    	        	  String str=methodnamelist.get(i);
    	        	  if (str.contains("/")) {
    	        		  str=str.substring(str.lastIndexOf("/")+1);
    	        		  methodnamelist.set(i, str);
    				}
    	        	  
    				
    			}
    	          
    	          
    	          for (int i = 0; i < methodnamelist.size(); i++) {
    	        	  currentMethodName=currentMethodName+methodnamelist.get(i);
    			}
    	          
    	          
    	          
    	         oms.setMethodName(methodName);
    	         oms.setOverrideName(currentMethodName);
    	         oms.setOverrideType(className);
    	         overrideMethodFinds.add(oms);
	     
	    	 System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
	     
	     }
	 
	     /* search in super interfaces also */
	     ITypeBinding[] interfaceBinds = typeBind.getInterfaces();
	     for(ITypeBinding interfaceBind : interfaceBinds) {   
	          if((overridenMethodBind = matchMethodInType(methodBind, superTypeBind)) == null)
	               findOverrideInHierarchy(methodBind, superTypeBind,overrideMethodFinds);
	          else
	          {
	        	  
	        	  OverrideMethods oms=new OverrideMethods();
	       	     
	     	     //overridenMethodBind.getKey() :  Lorg/eclipse/ui/part/EditorPart;.doSave(Lorg/eclipse/core/runtime/IProgressMonitor;)V
	     	          String className=overridenMethodBind.getKey().substring(0,(overridenMethodBind.getKey()).indexOf(";"));
	     	                 if (className.contains("/")) {
	     	                	 className=className.substring(className.lastIndexOf("/")+1);
	     					}
	     	                 String methodName=overridenMethodBind.getName();
	     	                 
	     	                 
	     	          String methodNamestr=overridenMethodBind.getKey().substring((overridenMethodBind.getKey()).lastIndexOf(".")+1);
	     	                 methodNamestr=methodNamestr.substring(0, methodNamestr.length()-1);
	     	                 
	     	         
	     	        		  	          
	     	          String currentMethodName=methodName+"(";
	     	                 
	     	          List<String> methodnamelist=CommUtil.arrayToList(methodNamestr.split("[;]"));
	     	          for (int i = 0; i < methodnamelist.size(); i++) {
	     	        	  String str=methodnamelist.get(i);
	     	        	  if (str.contains("/")) {
	     	        		  str=str.substring(str.lastIndexOf("/")+1);
	     	        		  methodnamelist.set(i, str);
	     				}
	     	        	  
	     				
	     			}
	     	          
	     	          
	     	          for (int i = 0; i < methodnamelist.size(); i++) {
	     	        	  currentMethodName=currentMethodName+methodnamelist.get(i);
	     			}
	     	          
	     	          
	     	          
	     	         oms.setMethodName(methodName);
	     	         oms.setOverrideName(currentMethodName);
	     	         oms.setOverrideType(className);
	     	         overrideMethodFinds.add(oms);
	 	    
	               System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
	     
	          }
	          
	     } 
		
	}

}
