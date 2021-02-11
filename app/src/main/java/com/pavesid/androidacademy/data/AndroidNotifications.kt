package com.pavesid.androidacademy.data

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import androidx.core.net.toUri
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.db.movies.MovieEntity
import com.pavesid.androidacademy.ui.MainActivity

class AndroidNotifications(private val context: Context) : Notifications {

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MOVIES) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_NEW_MOVIES, IMPORTANCE_HIGH)
                    .setName(context.getString(R.string.channel_new_movie))
                    .setDescription(context.getString(R.string.channel_new_movie_description))
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(movie: MovieEntity) {
        val contentUri = "https://androidacademy.pavesid.com/movies/${movie.id}".toUri()

        val builder = NotificationCompat.Builder(context, CHANNEL_NEW_MOVIES)
            .setContentTitle(movie.title)
            .setContentText(context.getString(R.string.new_movie))
            .setSmallIcon(R.drawable.ic_outline_local_movies_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )

        notificationManagerCompat.notify(MOVIE_TAG, movie.id.toInt(), builder.build())
    }

    override fun dismissNotification(movieId: Long) {
        notificationManagerCompat.cancel(MOVIE_TAG, movieId.toInt())
    }

    companion object {
        private const val CHANNEL_NEW_MOVIES = "new_movies"

        private const val REQUEST_CONTENT = 1

        private const val MOVIE_TAG = "movie"
    }
}
