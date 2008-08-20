package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.CommandAction;
import de.zib.gndms.logic.model.config.ConfigActionTestBase;
import org.testng.annotations.*;

import java.io.PrintWriter;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 16:11:02
 */
@SuppressWarnings({
        "IOResourceOpenedButNotSafelyClosed", "UseOfSystemOutOrSystemErr",
        "HardcodedFileSeparator" })
public class SetupSubspaceActionTest extends ConfigActionTestBase {
    @Parameters({"dbPath", "dbName"})
    public SetupSubspaceActionTest(final String dbPath, @Optional("c3grid") final String Name) {
        super(dbPath, Name);
    }


    @BeforeClass(groups={"db", "action", "config"})
    public void init() {
        removeDbPath();
    }

    @AfterClass(groups={"db", "action", "config"})
    public void shutdownDb() {
        tryCloseEMF();
    }

    @Test(groups={"db", "action", "config"})
    public void testExecute() throws CommandAction.ParameterTools.ParameterParseException {
        SetupSubspaceAction action;

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:create; scope:http://www.c3grid.de/G2; name:Staging; size:10; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:update; scope:http://www.c3grid.de/G2; name:Staging; size:20; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }

        action = new SetupSubspaceAction();
        try {
            prepareConfigAction(action, new PrintWriter(System.out), "mode:delete; scope:http://www.c3grid.de/G2; name:Staging; size:20; path:/tmp");
            action.call();
        }
        finally { action.getEntityManager().close(); }
    }
}
