package lang.annotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeApply;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

@SupportedAnnotationTypes({ "lang.annotation.Sql" })
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

	private Messager messager;
	private JavacTrees trees;
	private TreeMaker treeMaker;
	private Names names;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
		this.trees = JavacTrees.instance(processingEnv);
		Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
		this.treeMaker = TreeMaker.instance(context);
		this.names = Names.instance(context);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element e : roundEnv.getElementsAnnotatedWith(Sql.class)) {
			if (e.getKind() == ElementKind.METHOD) {
				JCTree jcTree = trees.getTree(e);

				jcTree.accept(new TreeTranslator() {

					@Override
					public void visitMethodDef(JCMethodDecl paramJCMethodDecl) {
						ListBuffer<JCExpression> arguments = new ListBuffer<JCExpression>();
						arguments.add(treeMaker.Ident(names.fromString("String")));
						arguments.add(treeMaker.Ident(names.fromString("Object")));
						Name name = names.fromString("params");
						JCTypeApply jcTypeApply = treeMaker.TypeApply(treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("java")), names.fromString("util")), names.fromString("Map")),
								arguments.toList());
						JCTypeApply jcTypeApplyForInit = treeMaker
								.TypeApply(treeMaker.Select(treeMaker.Select(treeMaker.Ident(names.fromString("java")), names.fromString("util")), names.fromString("HashMap")), arguments.toList());
						JCNewClass jcNewClass = treeMaker.NewClass(null, null, jcTypeApplyForInit, List.nil(), null);
						JCVariableDecl varDecl = treeMaker.VarDef(treeMaker.Modifiers(0), name, jcTypeApply,
								jcNewClass);

						ListBuffer<JCStatement> statementList = new ListBuffer<JCStatement>();
						statementList.add(varDecl);

						List<JCVariableDecl> params = paramJCMethodDecl.params;
						for (JCVariableDecl jcVariableDecl : params) {
							ListBuffer<JCExpression> args4Method = new ListBuffer<JCExpression>();
							args4Method.add(treeMaker.Literal(jcVariableDecl.name.toString()));
							args4Method.add(treeMaker.Ident(jcVariableDecl));
							JCFieldAccess jcFieldAccess = treeMaker.Select(treeMaker.Ident(name),
									names.fromString("put"));
							List<JCExpression> l = args4Method.toList();
							JCMethodInvocation meth = treeMaker.Apply(null, jcFieldAccess, l);
							statementList.add(treeMaker.Exec(meth));
						}

						statementList.add(paramJCMethodDecl.body.stats.last());
						paramJCMethodDecl.getBody().stats.setTail(statementList.toList());

						JCStatement statement = paramJCMethodDecl.body.stats.last();

						statement.accept(new TreeTranslator() {

							@Override
							public void visitReturn(JCReturn paramJCReturn) {
								paramJCReturn.expr.accept(new TreeTranslator() {

									@Override
									public void visitApply(JCMethodInvocation paramJCMethodInvocation) {
										ListBuffer<JCExpression> list = new ListBuffer<JCExpression>();
										list.add(treeMaker.Ident(name));
										paramJCMethodInvocation.args.setTail(list.toList());
										super.visitApply(paramJCMethodInvocation);
									}

								});
								super.visitReturn(paramJCReturn);
							}

						});

						super.visitMethodDef(paramJCMethodDecl);
					}

				});
			}
		}
		return true;
	}

}
