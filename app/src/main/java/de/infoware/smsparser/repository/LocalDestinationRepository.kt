package de.infoware.smsparser.repository

import de.infoware.smsparser.data.DestinationInfo
import de.infoware.smsparser.data.storage.DestinationDatabase
import de.infoware.smsparser.data.storage.DestinationEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Actual implementation of repository for the local DataBase
 */
class LocalDestinationRepository(private val destinationDatabase: DestinationDatabase) :
    DestinationRepository {

    override fun getAllDestinationInfo(): Single<List<DestinationInfo>> {
        return destinationDatabase
            .destinationDao()
            .getAll()
            .map {
                val result = ArrayList<DestinationInfo>()
                it.forEach { entity ->
                    result.add(
                        DestinationInfo(
                            entity.lat,
                            entity.lon,
                            entity.reason,
                            entity.addedTimestamp,
                            entity.blueLightNavigation,
                            entity.alreadyShown,
                            entity.uid
                        )
                    )
                }
                return@map result
            }
    }

    override fun insertDestinationInfo(destinationInfo: DestinationInfo): Completable {
        return destinationDatabase
            .destinationDao()
            .insert(
                DestinationEntity(
                    destinationInfo.lat,
                    destinationInfo.lon,
                    destinationInfo.reason,
                    destinationInfo.addedTimestamp,
                    destinationInfo.blueLightRouting
                )
            )
    }

    override fun updateNavigatedStatus(destinationInfo: DestinationInfo): Maybe<Int> {
        return destinationDatabase
            .destinationDao()
            .updateNavigatedStatus(
                destinationInfo.uidInDataSource,
                destinationInfo.alreadyNavigated
            )
    }

    override fun deleteAll(): Maybe<Int> {
        return destinationDatabase
            .destinationDao()
            .deleteAll()
    }
}