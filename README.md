*ant-fit* is a collection of Ant tasks to execute Fit (the Framework for Interactive Tests written in Java by Ward Cunningham) from within XML build files. This collection is different from the original [AntFit][] in that it offers a task with additional flexibility when running tests, by allowing the developer to specify the exact number of right, wrong, ignores and exceptions expected; this features comes handy when a specification is not entirely implemented, but you still want to identify the partial work you have done as a success (i.e. not something that breaks or stops the build).

### Examples

*ant-fit* includes two tasks: `fitreport` and `fit`.

The `fitreport` task allows you to run a bunch of specification documents through Fit and produce the corresponding reports. You define this task by the following Ant XML element:

    <taskdef name="fitreport" classname="org.apache.tools.ant.taskdefs.optional.fit.FitReportTask" />

The task allows you to specify the destination directory where the reports will be written and an option to indicate if the wiki syntax should be used for the Fit documents. The target also supports filesets.

    <fitreport destdir="${test.report.dir}" useWiki="false">
        <fileset dir="${test.doc.dir}">
            <include name="myFirstAcceptanceTestSpecification.html"/>
            <include name="aSecondAcceptanceTestSpecification.html"/>
            <!-- ... -->
            <include name="myLastAcceptanceTestSpecification.html"/>
        </fileset>
    </fitreport>

The `fit` task is a bit more flexible: it allows you to specify the number of right, wrong, ignores and exceptions expected for each document. This is useful when you are in the middle of working on a big specification: some parts may not be complete, but need to be run and verified as they are, indicating a success even if there are still some cases that are not successful, or at least avoiding to break or stop the build. In such cases, you don't even need to see the reports, so the fit task does not generate them.

    <taskdef name="fit" classname="org.apache.tools.ant.taskdefs.optional.fit.FitTask" />

The task allows you to specify the figures expected from each test document.

    <fit dir="${test.doc.dir}">
        <document name="myFirstAcceptanceTestSpecification.html"
                  right="37" wrong="7" ignores="0" exceptions="5"/>
        <document name="aSecondAcceptanceTestSpecification.html"
                  right="42" wrong="0" ignores="4" exceptions="0"/>
        <!-- ... -->
        <document name="myLastAcceptanceTestSpecification.html"
                  right="13" wrong="1" ignores="0" exceptions="1"/>
    </fit>

If no attributes are present so that the figures are not specified, the `fit` task presumes that you want the whole specification to pass.

### Credits

These tasks have been created in 2006 to integrate Fit tests for the ISO Prolog acceptance suite in the building process of [tuProlog][]. These tasks have not been further developed since. I consider it to be in an abandoned state. (Well, pretty much everything Fit-related, apart from Fitnesse, seems in an abandoned state to me, these days.) This repository exists primarily to avoid myself losing the work and the memories of the sunny summer day when I was at the seaside working on this library.

[AntFit]: http://www.cmdev.com/antfit/
[tuProlog]: http://tuprolog.sourceforge.net/