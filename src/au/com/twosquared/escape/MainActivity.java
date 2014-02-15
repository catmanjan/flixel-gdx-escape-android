package au.com.twosquared.escape;

import escape.EscapeGame;
import escape.custom.ILeaderboard;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends BaseGameActivity {

	private String LEADERBOARD_ID = "";
	private int REQUEST_LEADERBOARD = 1;
	private boolean signInSuccess;
	private ILeaderboard leaderboard = new ILeaderboard() {

		@Override
		public void showLeaderboard() {
			boolean connected = getGamesClient().isConnected();

			if (!connected) {
				beginUserInitiatedSignIn();

				if (signInSuccess) {
					connected = true;
				}
			}

			if (connected) {
				startActivityForResult(
						getGamesClient().getLeaderboardIntent(LEADERBOARD_ID),
						REQUEST_LEADERBOARD);
			}
		}

		@Override
		public void highscore(int score) {
			getGamesClient().submitScore(LEADERBOARD_ID, score);
		}

		@Override
		public boolean isConnected() {
			return getGamesClient().isConnected();
		}

	};

	private boolean tried = false;

	static EscapeGame game = new EscapeGame();

	public MainActivity() {
		super(game);
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		game.setLeaderboard(leaderboard);
	}

	@Override
	public void onSignInFailed() {
		Log.d("Escape", "onSignInFailed");

		if (!tried) {
			tried = true;
			beginUserInitiatedSignIn();
		}
	}

	@Override
	public void onSignInSucceeded() {
		signInSuccess = true;
		Log.d("Escape", "onSignInSucceeded");
	}

}