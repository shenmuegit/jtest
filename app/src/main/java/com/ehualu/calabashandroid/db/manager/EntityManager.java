package com.ehualu.calabashandroid.db.manager;

import com.ehualu.calabashandroid.db.TestFileDao;
import com.ehualu.calabashandroid.db.UploadEntityDao;
import com.ehualu.calabashandroid.db.UploadPieceEntityDao;
import com.ehualu.calabashandroid.db.entity.UploadEntity;
import com.ehualu.calabashandroid.db.entity.UploadPieceEntity;

import java.util.List;

/**
 * 数据库表管理类
 */
public class EntityManager {

    private static EntityManager entityManager;
    private TestFileDao testFileDao;
    private UploadEntityDao uploadEntityDao;
    private UploadPieceEntityDao uploadPieceEntityDao;

    private EntityManager() {
    }

    public static EntityManager getInstance() {
        if (entityManager == null) {
            synchronized (EntityManager.class) {
                if (entityManager == null) {
                    entityManager = new EntityManager();
                }
            }
        }
        return entityManager;
    }

    public TestFileDao getTestFileDao() {
        testFileDao = DBManager.getInstance().getDaoSession().getTestFileDao();
        return testFileDao;
    }

    public UploadEntityDao getUploadEntityDao() {
        uploadEntityDao = DBManager.getInstance().getDaoSession().getUploadEntityDao();
        return uploadEntityDao;
    }

    public UploadPieceEntityDao getUploadPieceEntityDao() {
        uploadPieceEntityDao = DBManager.getInstance().getDaoSession().getUploadPieceEntityDao();
        return uploadPieceEntityDao;
    }

    public String updateUploadEntity(String taskId, int status, String targetPath, int fileStatus) {
        //先查询
        UploadEntity uploadEntity = getUploadEntityDao().queryBuilder()
                .where(UploadEntityDao.Properties.TaskId.eq(taskId)).unique();
        uploadEntity.setStatus(status);
        uploadEntity.setTargetPath(targetPath);
        uploadEntity.setUpdateTime(System.currentTimeMillis());
        uploadEntity.setFileStatus(0);
        getUploadEntityDao().update(uploadEntity);
        return uploadEntity.getDirId();
    }

    /**
     * 清空所有的上传完成的任务，这里不能清空表，因为本地文件还需要根据这张表来区分是否上传
     */
    public void deleteAllFinishTask() {
        List<UploadEntity> entities = getUploadEntityDao().queryBuilder().where(UploadEntityDao.Properties.Status.eq(4)).list();
        for (UploadEntity e : entities) {
            e.setFileStatus(1);
        }
        getUploadEntityDao().updateInTx(entities);
    }

    /**
     * 更新上传表的进度
     */
    public void updateUploadPieceProgress(String taskId, long current) {
        UploadEntity uploadEntity = getUploadEntityDao().queryBuilder()
                .where(UploadEntityDao.Properties.TaskId.eq(taskId)).unique();
        uploadEntity.setProgress(current);
        getUploadEntityDao().update(uploadEntity);
    }

    public UploadEntity queryFinishUploadTask(String taskId) {
        return getUploadEntityDao().queryBuilder().where(UploadEntityDao.Properties.TaskId.eq(taskId)).unique();
    }

    /**
     * 更新分片表的状态
     */
    public void updatePieceStatus(String chunkId, boolean status) {
        UploadPieceEntity uploadPieceEntity = getUploadPieceEntityDao().queryBuilder()
                .where(UploadPieceEntityDao.Properties.ChunkId.eq(chunkId)).unique();
        if (status) {
            uploadPieceEntity.setStatus(4);
        } else {
            uploadPieceEntity.setStatus(3);
        }
        getUploadPieceEntityDao().update(uploadPieceEntity);
    }

    /**
     * 查询该任务所有已经完成的分片的数量
     *
     * @param taskId
     */
    public int queryAllFinishPiece(String taskId) {
        return getUploadPieceEntityDao().queryBuilder()
                .where(UploadPieceEntityDao.Properties.TaskId.eq(taskId))
                .where(UploadPieceEntityDao.Properties.Status.eq(4)).list().size();
    }
}
