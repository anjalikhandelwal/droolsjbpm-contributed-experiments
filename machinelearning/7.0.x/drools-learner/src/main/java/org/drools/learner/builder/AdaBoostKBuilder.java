package org.drools.learner.builder;

import java.util.ArrayList;

import org.drools.learner.AttributeValueComparator;
import org.drools.learner.DecisionTree;
import org.drools.learner.Instance;
import org.drools.learner.InstanceList;
import org.drools.learner.Stats;
import org.drools.learner.tools.LoggerFactory;
import org.drools.learner.tools.SimpleLogger;
import org.drools.learner.tools.Util;

public class AdaBoostKBuilder extends DecisionTreeBuilder {

    private static SimpleLogger flog = LoggerFactory.getUniqueFileLogger( AdaBoostKBuilder.class, SimpleLogger.DEFAULT_LEVEL );
    private static SimpleLogger slog = LoggerFactory.getSysOutLogger( AdaBoostKBuilder.class, SimpleLogger.DEBUG );

    private TreeAlgo algorithm = TreeAlgo.BOOST_K; // default bagging, TODO boosting

    //	private double trainRatio = Util.TRAINING_RATIO, testRatio = Util.TESTING_RATIO;

    private static int FOREST_SIZE = 10;
    private static final double TREE_SIZE_RATIO = 0.9;
    private static final boolean WITH_REP = false;

    private ArrayList<DecisionTree> forest;
    private ArrayList<Double> classifierAccuracy;

    private Solution bestSolution;
    //private Learner trainer;

    private DecisionTreeMerger merger;

    public AdaBoostKBuilder() {
        //this.trainer = _trainer;
        merger = new DecisionTreeMerger();
    }

    //	public void setTrainRatio(double ratio) {
    //		trainRatio = ratio;
    //	}
    //	public void setTestRatio(double ratio) {
    //		testRatio = ratio;
    //	}
    public void internalBuild( SolutionSet sol, Learner trainer ) {

        trainer.setInputSpec( sol.getInputSpec() );

        if ( sol.getInputSpec().getTargets().size() > 1 ) {
            //throw new FeatureNotSupported("There is more than 1 target candidates");
            if ( flog.error() != null )
                flog.error().log( "There is more than 1 target candidates" );
            System.exit( 0 );
            // TODO put the feature not supported exception || implement it
        } else if ( trainer.getTargetDomain().getCategoryCount() <= 2 ) {
            if ( flog.warn() != null )
                flog.warn().log( "The target domain is binary!!! Do u really need that one" );
        }

        int N = sol.getTrainSet().getSize();
        //_trainer.setTrainingDataSize(N); not only N data is fed. 

        int K = trainer.getTargetDomain().getCategoryCount();
        int M = (int) ( TREE_SIZE_RATIO * N );
        trainer.setTrainingDataSizePerTree( M );
        /* M data fed to each tree, there are FOREST_SIZE trees */
        trainer.setTrainingDataSize( M * FOREST_SIZE );

        forest = new ArrayList<DecisionTree>( FOREST_SIZE );
        classifierAccuracy = new ArrayList<Double>( FOREST_SIZE );
        //Initialize the weight wij  (i = 1,...,m (number of instances) and j = 1,...,K (number of classes))
        double[][] weight = new double[M][K];
        for ( int indexI = 0; indexI < M; indexI++ ) {
            for ( int indexJ = 0; indexJ < K; indexJ++ ) {
                Instance instI = sol.getTrainSet().getInstance( indexI );

                Object instanceTarget = instI.getAttrValue( trainer.getTargetDomain().getFReferenceName() );
                Object instanceTargetCategory = trainer.getTargetDomain().getCategoryOf( instanceTarget );
                Object targetCategory = trainer.getTargetDomain().getCategory( indexJ );

                if ( AttributeValueComparator.instance.compare( instanceTargetCategory, targetCategory ) == 0 ) {
                    //if y_i == u_j => the instance i is in class n.
                    weight[indexI][indexJ] = 0;
                } else {
                    // mistake
                    weight[indexI][indexJ] = 1;
                }

            }
        }

        int i = 0;
        int[] bag;
        while ( i++ < FOREST_SIZE ) {
            // a.  Normalize wij
            if ( WITH_REP )
                bag = Util.bagWRep( M, N );
            else
                bag = Util.bagWoRep( M, N );

            // b. Train h_t(x) by minimizing loss function

            InstanceList workingInstances = sol.getTrainSet().getInstances( bag );
            DecisionTree dt = trainer.trainTree( workingInstances );
            dt.setID( i );

            double error = 0.0;
            SingleTreeTester t = new SingleTreeTester( dt );
            for ( int indexI = 0; indexI < M; indexI++ ) {
                Integer result = t.test( sol.getTrainSet().getInstance( indexI ) );
                if ( result == Stats.INCORRECT ) {

                    //error += distribution.get(index_i);
                }
            }

            if ( error > 0.0f ) {
                double alpha = Util.ln( ( 1.0d - error ) / error ) / 2.0d;

                if ( error < 0.5d ) {
                    // The classification accuracy of the weak classifier
                    classifierAccuracy.add( alpha );

                    double normFact = 0.0d;
                    // Update the weight matrix wij:
                    for ( int indexI = 0; indexI < M; indexI++ ) {
                        Integer result = t.test( sol.getTrainSet().getInstance( indexI ) );//TODO dont need to test two times
                        switch ( result ) {
                            case Stats.INCORRECT:
                            //distribution.set(index_i, distribution.get(index_i) * Util.exp(alpha));
                            break;
                            case Stats.CORRECT:
                            //distribution.set(index_i, distribution.get(index_i) * Util.exp(-1.0d * alpha));
                            break;
                            case Stats.UNKNOWN:
                                if ( slog.error() != null )
                                    slog.error().log( "Unknown situation bok" );
                                System.exit( 0 );
                            break;
                        }
                        //norm_fact += distribution.get(index_i);
                    }
                    // Normalization of the weights
                    for ( int indexI = 0; indexI < M; indexI++ ) {
                        //distribution.set(index_i, distribution.get(index_i) / norm_fact);
                    }
                } else {
                    if ( slog.debug() != null )
                        slog.debug().log( "The error=" + error + " alpha:" + alpha + "\n" );
                    if ( slog.error() != null )
                        slog.error().log( "error:" + error + " alpha will be negative and the weights of the training samples will be updated in the wrong direction" + "\n" );
                    FOREST_SIZE = i - 1;//ignore the current tree
                    break;
                }
            }

            else {
                if ( slog.stat() != null )
                    slog.stat().log( "All instances classified correctly TERMINATE, forest size:" + i + "\n" );
                // What to do here??
                FOREST_SIZE = i;
                classifierAccuracy.add( 10.0 ); // TODO add a very big number

            }

            forest.add( dt );

            // the DecisionTreeMerger will visit the decision tree and add the paths that have not been seen yet to the list
            merger.add( dt );

            if ( slog.stat() != null )
                slog.stat().stat( "." );

        }
        // TODO how to compute a best tree from the forest
        //_trainer.setBestTree(forest.get(0));
        //		best = merger.getBest();
        //		if (best == null)
        //			best = forest.get(0);
        //this.c45 = dt;
    }

    public ArrayList<DecisionTree> getTrees() {
        return forest;
    }

    public ArrayList<Double> getAccuracies() {
        return classifierAccuracy;
    }

    public TreeAlgo getTreeAlgo() {
        return algorithm; //TreeAlgo.BAG; // default
    }

    public void clearForest( int j ) {
        //forest = null;
        for ( int i = forest.size() - 1; i > j; i-- )
            forest.remove( i );

    }

    public Solution getBestSolution() {
        return bestSolution;
    }
}
