package com.noahbres.meepmeep.roadrunner

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.*
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder
import java.util.*

class DriveShim(driveTrainType: DriveTrainType, private val constraints: Constraints) {
    private val velConstraint = when (driveTrainType) {
        DriveTrainType.MECANUM -> MinVelocityConstraint(
            listOf(
                AngularVelocityConstraint(constraints.maxAngVel),
                MecanumVelocityConstraint(constraints.maxVel, constraints.trackWidth)
            )
        )
        DriveTrainType.TANK -> MinVelocityConstraint(
            listOf(
                AngularVelocityConstraint(constraints.maxAngVel),
                TankVelocityConstraint(constraints.maxVel, constraints.trackWidth)
            )
        )
    }

    private val accelConstraint = ProfileAccelerationConstraint(constraints.maxAccel)

    fun trajectorySequenceBuilder(startPose: Pose2d): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(startPose, velConstraint, accelConstraint, constraints)
    }

    @JvmOverloads
    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double = startPose.heading): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startHeading, velConstraint, accelConstraint)
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, reversed, velConstraint, accelConstraint)
    }
}