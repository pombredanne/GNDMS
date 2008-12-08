package de.zib.gndms.logic.model;

import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.action.LogAction;
import de.zib.gndms.model.gorfx.AbstractTask;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.stuff.copy.Copier;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.util.GregorianCalendar;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.09.2008 Time: 11:26:48
 */
@SuppressWarnings({ "AbstractMethodCallInConstructor" })
public abstract class TaskAction extends AbstractModelAction<AbstractTask, AbstractTask> implements LogAction
{
    private TaskExecutionService service;
    private Log log;
    private String wid;
    private Class<? extends AbstractTask> taskClass;
    private AbstractTask backup;

    private static final class ShutdownTaskActionException extends RuntimeException {
        private static final long serialVersionUID = 2772466358157719820L;

        ShutdownTaskActionException(final Exception eParam) {
            super(eParam);
        }
    }

    protected static class TransitException extends RuntimeException {
        private static final long serialVersionUID = 1101501745642141770L;

        protected final TaskState newState;

        protected TransitException(final @NotNull TaskState newStateParam) {
            super();
            newState = newStateParam;
        }

        protected TransitException(final @NotNull TaskState newStateParam,
                                   final @NotNull RuntimeException cause) {
            super(cause);
            newState = newStateParam;
        }

        public boolean isDemandingAbort() { return false; }
    }

    protected static class FailedException extends TransitException {
        private static final long serialVersionUID = -4220356706557491625L;

        protected FailedException(final @NotNull RuntimeException cause) {
            super(TaskState.FAILED, cause);
        }

        @Override
        public boolean isDemandingAbort() { return true; }
    }

    protected static class FinishedException extends TransitException {
        private static final long serialVersionUID = 196914329532915066L;

        protected final Serializable result;

        protected FinishedException(final Serializable resultParam) {
            super(TaskState.FINISHED);
            result = resultParam;
        }
    }

    private static class StopException extends TransitException {
        private static final long serialVersionUID = 7783981039310846994L;


        protected StopException(final @NotNull TaskState newStateParam) {
            super(newStateParam);
        }

        @Override
        public boolean isDemandingAbort() { return true; }
    }

    public TaskAction() {
        super();
    }

    
    public TaskAction(final @NotNull EntityManager em, final @NotNull AbstractTask model) {
        super();
        initFromModel(em, model);
    }


    public void initFromModel(final EntityManager em, AbstractTask model) {

        boolean wasActive = em.getTransaction().isActive();
        if (!wasActive)
            em.getTransaction().begin();
        try {
            final boolean contained = em.contains(model);
            if (! contained) {
                try {
                    em.persist(model);
                }
                catch (EntityExistsException e) {
                    model = em.merge(model);
                }
            }
            if (!wasActive)
                em.getTransaction().commit();
            setOwnEntityManager(em);
            setModelAndBackup(model);
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }


    public TaskAction(final @NotNull EntityManager em, final @NotNull String pk, Class<? extends AbstractTask> cls ) {
        super();
        setTaskClass( cls );
        initFromPk(em, pk);
    }


    public void initFromPk(final EntityManager em, final String pk) {
        boolean wasActive = em.getTransaction().isActive();
        if (!wasActive) em.getTransaction().begin();
        try {
            final AbstractTask model = em.find(getTaskClass(), pk);
            if (model == null)
                throw new IllegalArgumentException("Model not found for pk: " + pk);
            if (!wasActive) em.getTransaction().commit();
            setOwnEntityManager(em);
            setModelAndBackup(model);
        }
        finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }


    @Override
    public void setModel(final @NotNull AbstractTask mdl) {
        super.setModel(mdl);    // Overridden method
        wid = mdl.getWid();
        taskClass = mdl.getClass();
    }


    protected void setModelAndBackup( final @NotNull AbstractTask mdl ) {
        setModel( mdl );
        setBackup( Copier.copy( false, mdl ) );
    }



    protected @NotNull Class<? extends AbstractTask> getTaskClass() {
        return taskClass;
    }


    protected void setTaskClass( Class<? extends AbstractTask> cls ) {
        taskClass = cls;
    }


    public TaskExecutionService getService() {
        if (service == null) {
            final TaskAction taskAction = nextParentOfType(TaskAction.class);
            return taskAction == null ? null : taskAction.getService();
        }
        return service;
    }


    public void setService(final @NotNull TaskExecutionService serviceParam) {
        if (service == null)
            service = serviceParam;
        else
           throw new IllegalStateException("Can't overwrite service");
    }


    @Override
    protected final boolean isExecutingInsideTransaction() {
        return false;
    }


    @Override
    public AbstractTask call() throws RuntimeException {
        try {
            final AbstractTask task = getModel();
            if (task != null) 
                WidAux.initWid(getModel().getWid());
            return super.call();    // Overridden method
        }
        finally {
            WidAux.removeWid();
        }
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown", "ObjectAllocationInLoop" })
    @Override
    public AbstractTask execute(final @NotNull EntityManager em) {
        boolean first = true;
        TransitException curEx = null;
        do {
            // for debugging
            final String id = getModel().getId();
            final TaskState curState = getModel().getState();
            try {
                try {
                    if (first) {
                        try {
                            trace("transit(null)", null);
                            transit(null);
                        }
                        finally {
                            first = false;
                        }
                    }
                    else {
                        trace("transit(" + curEx.newState + ')', null);
                        transit(curEx.newState);
                    }
                    // if we come here, transit has failed
                    throw new IllegalStateException("No proper TransitException thrown");
                }
                /* On stop: finish loop and return model to caller */
                catch (final StopException newEx) {
                    trace("markAsDone()", null);
                    markAsDone();
                    curEx = null;
                }
                /* On transit: switch to next state according to newEx */
                catch (final TransitException newEx) {
                    curEx = newEx;
                }
                /* On shutdown: Do not set the task to failed, simply throw e */
                catch (final ShutdownTaskActionException e) {
                    throw e;
                }
                /* On runtime ex: Set task to failed */
                catch (RuntimeException e) {
                    trace("catch(RuntimeException)", e);
                    /* Cant go to FAILED after FINISHED, i.e. onFinish must never fail */
                    if (TaskState.FINISHED.equals(getModel().getState()))
                        throw e;                             // todo log this one
                    else
                        fail(e);
                }
            }
            /* If fail was called due to a RuntimeException in above try block, use resulting
               exception for the next state transition.  Ensures that the failed state of the
               task will be persisted.
             */
            catch (TransitException newEx) {
                curEx = newEx;
            }
        } while (curEx != null);
        trace("return getModel()", null);
        return getModel();
    }


    @SuppressWarnings({ "HardcodedFileSeparator" })
    protected void trace(final @NotNull String userMsg, final Throwable cause) {
        final Log log1 = getLog();
        final AbstractTask model = getModel();
        final String msg;
        if (model == null)
            msg = userMsg;
        else {
            final TaskState state = model.getState();
            final String descr = model.getDescription();
            msg = "TA of AbstractTask " + model.getId()
                    + (state == null ? "" : '/' + state.toString()) + ':'
                    + (userMsg.length() > 0 ? ' ' : "") + userMsg
                    + (descr == null ? "" : " descr: '" + descr + '\'');
        }
       if (cause == null)
           log1.trace(msg);
        else
           log1.trace(msg, cause);

    }


    protected void refreshTaskResource() {
        try {
            getPostponedActions().getListener().onModelChange(getModel());
        }
        catch (RuntimeException e) {
            // intentionally ignored
        }
    }


    private void markAsDone() {
        final @NotNull AbstractTask model = getModel();
        if (! model.isDone()) {
            final EntityManager em = getEntityManager();
            try {
                em.getTransaction().begin();
                AbstractTask newModel = em.find(AbstractTask.class, getModel().getId());
                newModel.setDone(true);
                em.getTransaction().commit();
                setModelAndBackup(newModel);
            }
            catch (RuntimeException e) {
                throw e;
            }
            finally {
                if (em.getTransaction().isActive())
                    em.getTransaction().rollback();
            }
        }
    }

    @SuppressWarnings( { "CaughtExceptionImmediatelyRethrown", "ThrowableInstanceNeverThrown" } )
    private void transit(final TaskState newState) {

        final EntityManager em = getEntityManager();
        @NotNull AbstractTask model = getModel();

        // check the task lifetime
        if( model.getTerminationTime().compareTo( new GregorianCalendar( ) ) < 1 ) {
            getLog().debug(  "Task lifetime exceeded" );
            TxFrame tx = new TxFrame( em );
            boolean containt = false;
            try {
                // check if model is still there
                containt = em.contains( model );
                if( containt ) {
                    model.fail( new RuntimeException( "Task lifetime exceeded" ) );
                    getLog().debug(  "Try to persist task" );
                }
                tx.commit( );
            } catch ( Exception e ) {
                // exception here  doesn't really matter
                // task is doomed anyway
                e.printStackTrace(  );
            } finally {
                tx.finish();
            }
            // interrupt this thread
            getLog().debug(  "Stopping task thread" );
            //Thread.currentThread().interrupt();
            if( containt ) refreshTaskResource();
            stop( model );
        }

        try {
            em.getTransaction().begin();
            if (newState != null) {
                if (! em.contains(model)) {
                    try {
                        try {
                            em.persist(model);
                        }
                        catch (EntityExistsException e) {
                            rewindTransaction(em.getTransaction());
                            em.merge(model);
                        }
                    }
                    catch (RuntimeException e2) {
                            rewindTransaction(em.getTransaction());
                            final @NotNull AbstractTask newModel = em.find(AbstractTask.class, model.getId());
                            model.mold(newModel);
	                        newModel.refresh(em);
                            setModel(newModel);
                            model = getModel();
                        }
                    }
                }
            model.transit(newState);
            try {
                em.getTransaction().commit();
                // if model could be commited it has a clean state
                // refresh backup
                setBackup( Copier.copy( false, model ) );
            } catch ( Exception e ) {
                rewindTransaction(em.getTransaction());
                // if this point is reached s.th. is terribly foobared
                // restore backup and fail
                model = Copier.copy( false, backup );
                em.merge( model );
                // backup should be clean so commit mustn't fail.
                em.getTransaction().commit();
                model.fail( e );
            }

            final TaskState modelState = model.getState();
            refreshTaskResource();
            //noinspection HardcodedFileSeparator
            trace("on" + modelState + "()", null);
            transit(modelState, model);
        }
        // for debugging
        catch (RuntimeException e) {
            throw e;
        }
        finally {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
        }
    }


    private void rewindTransaction(final EntityTransaction txParam) {
        if (txParam.getRollbackOnly()) {
            txParam.rollback();
            txParam.begin();
        }
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    private void transit(final @NotNull TaskState newState, final @NotNull AbstractTask model) {
        switch (model.getState().transit(newState)) {
            case CREATED: onCreated(model); break;
            case CREATED_UNKNOWN: onUnknown(model); break;
            case INITIALIZED: onInitialized(model); break;
            case INITIALIZED_UNKNOWN: onUnknown(model); break;
            case IN_PROGRESS: onInProgress(model); break;
            case IN_PROGRESS_UNKNOWN: onUnknown(model); break;
            case FAILED:
                    if (! model.isDone())
                        onFailed(model);
                    /* safety catch-all */
                    throw new StopException(TaskState.FAILED);
            case FINISHED:
                    if (! model.isDone())
                        onFinished(model);
                    /* safety catch-all */
                    throw new StopException(TaskState.FINISHED);
            default:
                throw new IllegalStateException("Invalid or unsupported task state");
        }
    }

    protected TaskState onUnknown(final AbstractTask model) {
        throw new UnsupportedOperationException();
    }


    protected void onCreated(final AbstractTask model) {
        transitToState(TaskState.INITIALIZED);

    }

    protected void onInitialized(final AbstractTask model) {
        transitToState(TaskState.IN_PROGRESS);
    }

    protected abstract void onInProgress(final @NotNull AbstractTask model);


    protected void onFailed(final @NotNull AbstractTask model) {
        stop(model);
    }


    protected void onFinished(final @NotNull AbstractTask model) {
        stop(model);
    }


    protected static void transitToState(final @NotNull TaskState newState) {
        throw new TransitException(newState);
    }


    protected void fail(final @NotNull RuntimeException e) {
        getModel().fail(e);
        e.fillInStackTrace();
        throw new FailedException(e);
    }


    protected void finish(final Serializable result) {
        getModel().finish(result);
        throw new FinishedException(result);
    }


    @SuppressWarnings({ "MethodMayBeStatic" })
    protected void stop(final @NotNull AbstractTask model) {
        throw new StopException(model.getState());
    }

    
    protected final void shutdownIfTerminating(InterruptedException e) {
        if (getService().isTerminating())
            throw new ShutdownTaskActionException(e);
    }

    @SuppressWarnings({ "MethodMayBeStatic" })
    protected final void honorOngoingTransit(RuntimeException e) {
        if (isTransitException(e))
            throw e;
    }

    public static boolean isTransitException( Exception e ) {
        return e instanceof TransitException;
    }


    public static boolean isFinishedTransition( Exception e ) {
        return e instanceof FinishedException;
    }


    public static boolean isFailedTransition( Exception e ) {
        return e instanceof FailedException;
    }


    public Log getLog() {
        return log;
    }


    public void setLog(final Log logParam) {
        log = logParam;
    }

    
    protected AbstractTask getBackup() {
        return backup;
    }


    protected void setBackup( AbstractTask backup ) {
        this.backup = backup;
    }
}
