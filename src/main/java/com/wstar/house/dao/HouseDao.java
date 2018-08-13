package com.wstar.house.dao;

import com.wstar.house.entity.HouseItemEntity;
import com.wstar.house.util.DBUtil;
import com.wstar.house.util.ListUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HouseDao {
    static String INSERT_HOUSE_ITEM_SQL = "insert into t_house_item(house_name,detail_link,"
            + "region,detail_address,small_pic_url,small_pic_target_link,small_pic_path,"
            + "area_range,house_types,house_rooms,price) values (?,?,?,?,?,?,?,?,?,?,?)";


    public static Integer insertHouseItem(HouseItemEntity houseItemEntity) {
        QueryRunner runner = new QueryRunner();

        Connection conn = DBUtil.getInstance().getConnection();
        Integer key = 0;
        try {
            key = runner.insert(conn, INSERT_HOUSE_ITEM_SQL,
                    new ResultSetHandler<Integer>() {
                        @Override
                        public Integer handle(ResultSet resultSet) throws SQLException {
                            if (resultSet.next())
                                return resultSet.getInt(1);
                            else
                                return 0;
                        }
                    }, houseItemEntity.getHouseName(),
                        houseItemEntity.getDetailLink(),
                        houseItemEntity.getHouseAddress().getRegion(),
                        houseItemEntity.getHouseAddress().getDetailAddress(),
                        houseItemEntity.getHouseSmallPicture().getPictureUrl(),
                        houseItemEntity.getHouseSmallPicture().getTargetLink(),
                        houseItemEntity.getHouseSmallPicture().getPicturePath(),
                        houseItemEntity.getAreaRange(),
//                        ListUtil.joinBySeparator(houseItemEntity.ge(), ","),
                        ListUtil.joinBySeparator(houseItemEntity.getHouseRooms(), ","),
                        houseItemEntity.getPrice());
        } catch (Exception ex) {
            ex.printStackTrace();
            DbUtils.rollbackAndCloseQuietly(conn);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return key;
    }

    public static List<Integer> insertHouseItems(List<HouseItemEntity> houseItemEntitys) {
        QueryRunner runner = new QueryRunner();

        Connection conn = DBUtil.getInstance().getConnection();
        List<Integer> keys = new ArrayList<>();
        try {
            for (HouseItemEntity houseItemEntity : houseItemEntitys) {
                Integer key = runner.insert(conn, INSERT_HOUSE_ITEM_SQL,
                        new ResultSetHandler<Integer>() {
                            @Override
                            public Integer handle(ResultSet resultSet) throws SQLException {
                                if (resultSet.next())
                                    return resultSet.getInt(1);
                                else
                                    return 0;
                            }
                        }, houseItemEntity.getHouseName(),
                        houseItemEntity.getDetailLink(),
                        houseItemEntity.getHouseAddress().getRegion(),
                        houseItemEntity.getHouseAddress().getDetailAddress(),
                        houseItemEntity.getHouseSmallPicture().getPictureUrl(),
                        houseItemEntity.getHouseSmallPicture().getTargetLink(),
                        houseItemEntity.getHouseSmallPicture().getPicturePath(),
                        houseItemEntity.getAreaRange(),
//                        ListUtil.joinBySeparator(houseItemEntity.getHouseTypes(), ","),
                        ListUtil.joinBySeparator(houseItemEntity.getHouseRooms(), ","),
                        houseItemEntity.getPrice());
                keys.add(key);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            DbUtils.rollbackAndCloseQuietly(conn);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        return keys;
    }
}
