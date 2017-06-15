package me.saket.dank.ui.submission;

import android.support.annotation.Nullable;

import net.dean.jraw.models.CommentNode;

import me.saket.dank.R;
import me.saket.dank.data.VotingManager;
import me.saket.dank.utils.Animations;
import me.saket.dank.widgets.swipe.SwipeAction;
import me.saket.dank.widgets.swipe.SwipeActionIconView;
import me.saket.dank.widgets.swipe.SwipeActions;
import me.saket.dank.widgets.swipe.SwipeActionsHolder;
import me.saket.dank.widgets.swipe.SwipeableLayout;
import timber.log.Timber;

/**
 * Controls gesture actions on comments and submission details.
 */
public class CommentSwipeActionsProvider implements SwipeableLayout.SwipeActionIconProvider {

  private static final String ACTION_NAME_REPLY = "CommentReply";
  private static final String ACTION_NAME_OPTIONS = "CommentOptions";
  private static final String ACTION_NAME_UPVOTE = "CommentUpvote";
  private static final String ACTION_NAME_DOWNVOTE = "CommentDownvote";

  private final VotingManager votingManager;
  private final SwipeActions commentSwipeActions;

  public CommentSwipeActionsProvider(VotingManager votingManager) {
    this.votingManager = votingManager;

    commentSwipeActions = SwipeActions.builder()
        .startActions(SwipeActionsHolder.builder()
            .add(SwipeAction.create(ACTION_NAME_REPLY, R.color.list_item_swipe_reply, 1f))
            .add(SwipeAction.create(ACTION_NAME_OPTIONS, R.color.list_item_swipe_more_options, 1f))
            .build())
        .endActions(SwipeActionsHolder.builder()
            .add(SwipeAction.create(ACTION_NAME_DOWNVOTE, R.color.list_item_swipe_downvote, 1f))
            .add(SwipeAction.create(ACTION_NAME_UPVOTE, R.color.list_item_swipe_upvote, 1f))
            .build())
        .build();
  }

  public SwipeActions getSwipeActions() {
    return commentSwipeActions;
  }

  @Override
  public void showSwipeActionIcon(SwipeActionIconView imageView, @Nullable SwipeAction oldAction, SwipeAction newAction) {
    switch (newAction.name()) {
      case ACTION_NAME_OPTIONS:
        resetIconRotation(imageView);
        imageView.setImageResource(R.drawable.ic_more_horiz_24dp);
        break;

      case ACTION_NAME_REPLY:
        resetIconRotation(imageView);
        imageView.setImageResource(R.drawable.ic_reply_24dp);
        break;

      case ACTION_NAME_UPVOTE:
        if (oldAction != null && ACTION_NAME_DOWNVOTE.equals(oldAction.name())) {
          imageView.setRotation(180);
          imageView.animate().rotationBy(180).setInterpolator(Animations.INTERPOLATOR).setDuration(200).start();
        } else {
          resetIconRotation(imageView);
          imageView.setImageResource(R.drawable.ic_arrow_upward_24dp);
        }
        break;

      case ACTION_NAME_DOWNVOTE:
        if (oldAction != null && ACTION_NAME_UPVOTE.equals(oldAction.name())) {
          imageView.setRotation(0);
          imageView.animate().rotationBy(180).setInterpolator(Animations.INTERPOLATOR).setDuration(200).start();
        } else {
          resetIconRotation(imageView);
          imageView.setImageResource(R.drawable.ic_arrow_downward_24dp);
        }
        break;

      default:
        throw new UnsupportedOperationException("Unknown swipe action: " + newAction);
    }
  }

  private void resetIconRotation(SwipeActionIconView imageView) {
    imageView.animate().cancel();
    imageView.setRotation(0);
  }

  public void performSwipeAction(SwipeAction swipeAction, CommentNode commentNode, SwipeableLayout swipeableLayout) {
    Timber.i("Action: %s", swipeAction.name());
    swipeableLayout.playRippleAnimation(swipeAction);  // TODO: Specify ripple direction.
  }
}