package workers;

import helpers.WordHelper;
import models.BoardModel;
import models.MatchModel;
import models.PointModel;
import models.SolveWorkerDataModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Worker to find all matches on the board from the given start point.
 */
public class SolveWorker implements Runnable {
    private final BoardModel board;
    private final Set<String> words;
    private final List<MatchModel> matches;
    private final PointModel startPoint;
    private final WordHelper wordHelper;

    /**
     * Initialize the solve worker.
     *
     * @param data       Data to use for solving.
     * @param startPoint Start point for solving.
     */
    public SolveWorker(SolveWorkerDataModel data, PointModel startPoint) {
        this.board = data.getBoard();
        this.words = data.getWords();
        this.matches = data.getMatches();
        this.wordHelper = data.getWordHelper();
        this.startPoint = startPoint;
    }

    /**
     * Start solving.
     */
    @Override
    public void run() {
        visitPoint("", startPoint, new HashSet<>());
    }

    /**
     * Visit the given point (if allowed) and check for a match in the list of words.
     *
     * @param currentWord   Current chain of letters.
     * @param point         Point to visit.
     * @param visitedPoints Points which already have been visited.
     */
    private void visitPoint(String currentWord, PointModel point, Set<PointModel> visitedPoints) {
        char letter = board.getLetter(point);
        visitedPoints.add(point);
        currentWord += letter;

        checkForMatch(currentWord, visitedPoints);

        // Prevent unnecessary operations if the current word doesn't have a match already.
        if (!wordHelper.hasPartialMatch(currentWord))
            return;

        List<PointModel> surroundingPoints = board.getSurroundingPoints(point);
        for (PointModel surroundingPoint : surroundingPoints) {
            // Check if the point has already been visited (this is not allowed).
            if (visitedPoints.contains(surroundingPoint))
                continue;

            // Create a copy of the set to prevent different paths from sharing visited points.
            Set<PointModel> visitedPointsCopy = new HashSet<>(visitedPoints);
            visitPoint(currentWord, surroundingPoint, visitedPointsCopy);
        }
    }

    /**
     * Check if the current word matches any of the words.
     *
     * @param currentWord   Word to check for.
     * @param visitedPoints Visited points for the current word.
     */
    private void checkForMatch(String currentWord, Set<PointModel> visitedPoints) {
        if (!words.contains(currentWord))
            return;

        MatchModel match = new MatchModel(currentWord, startPoint, visitedPoints);
        matches.add(match);
    }
}
