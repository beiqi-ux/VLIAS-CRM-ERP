package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.MenuDTO;
import com.example.vliascrm.entity.SysMenu;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.exception.ResourceNotFoundException;
import com.example.vliascrm.repository.SysMenuRepository;
import com.example.vliascrm.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Arrays;

/**
 * 菜单服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuRepository menuRepository;
    
    // 核心菜单编码，不允许删除和禁用
    private static final Set<String> CORE_MENU_CODES = new HashSet<>(Arrays.asList(
        "system",           // 系统管理
        "system:user",      // 用户管理
        "system:role",      // 角色管理
        "system:permission",// 权限管理
        "system:menu",      // 菜单管理
        "profile"           // 个人中心
    ));

    @Override
    @Transactional
    public SysMenu createMenu(MenuDTO menuDTO) {
        // 检查菜单编码是否存在
        if (menuRepository.existsByMenuCode(menuDTO.getMenuCode())) {
            throw new BusinessException("菜单编码已存在");
        }

        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        menu.setIsDeleted(false);

        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public SysMenu updateMenu(Long id, MenuDTO menuDTO) {
        SysMenu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜单不存在"));

        // 检查是否为核心菜单
        if (isCoreMenu(menu.getMenuCode())) {
            // 核心菜单不允许禁用
            if (menuDTO.getStatus() != null && menuDTO.getStatus() == 0) {
                throw new BusinessException("系统核心菜单不允许禁用");
            }
            // 核心菜单不允许修改菜单编码
            if (!menu.getMenuCode().equals(menuDTO.getMenuCode())) {
                throw new BusinessException("系统核心菜单不允许修改编码");
            }
        }

        // 如果修改了菜单编码，需要检查是否存在
        if (!menu.getMenuCode().equals(menuDTO.getMenuCode()) &&
                menuRepository.existsByMenuCode(menuDTO.getMenuCode())) {
            throw new BusinessException("菜单编码已存在");
        }

        BeanUtils.copyProperties(menuDTO, menu);
        menu.setUpdateTime(LocalDateTime.now());

        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long id) {
        SysMenu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜单不存在"));

        // 检查是否为核心菜单
        if (isCoreMenu(menu.getMenuCode())) {
            throw new BusinessException("系统核心菜单不允许删除");
        }

        // 检查是否有子菜单
        List<SysMenu> children = menuRepository.findByParentIdAndStatusAndIsDeletedOrderBySortAsc(id, 1, false);
        if (!children.isEmpty()) {
            throw new BusinessException("请先删除子菜单");
        }

        // 逻辑删除菜单
        menu.setIsDeleted(true);
        menu.setUpdateTime(LocalDateTime.now());
        menuRepository.save(menu);
    }

    /**
     * 判断是否为核心菜单
     * @param menuCode 菜单编码
     * @return 是否为核心菜单
     */
    private boolean isCoreMenu(String menuCode) {
        return CORE_MENU_CODES.contains(menuCode);
    }

    @Override
    public SysMenu getMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜单不存在"));
    }

    @Override
    public List<SysMenu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public List<MenuDTO> getMenuTree() {
        // 获取所有未删除的菜单
        List<SysMenu> allMenus = menuRepository.findAll().stream()
                .filter(m -> !m.getIsDeleted() && m.getStatus() == 1)
                .collect(Collectors.toList());

        // 转换为DTO
        List<MenuDTO> dtoList = allMenus.stream().map(m -> {
            MenuDTO dto = new MenuDTO();
            BeanUtils.copyProperties(m, dto);
            return dto;
        }).collect(Collectors.toList());

        // 构建树形结构
        Map<Long, List<MenuDTO>> parentMap = dtoList.stream()
                .collect(Collectors.groupingBy(MenuDTO::getParentId));

        // 获取顶级菜单
        List<MenuDTO> rootMenus = parentMap.getOrDefault(0L, new ArrayList<>());

        // 递归设置子菜单
        rootMenus.forEach(root -> setChildren(root, parentMap));

        return rootMenus;
    }

    private void setChildren(MenuDTO parent, Map<Long, List<MenuDTO>> parentMap) {
        List<MenuDTO> children = parentMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);
        children.forEach(child -> setChildren(child, parentMap));
    }

    @Override
    public List<SysMenu> getMenusByRoleId(Long roleId) {
        return menuRepository.findMenusByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return menuRepository.findMenusByUserId(userId);
    }

    @Override
    public List<MenuDTO> getUserMenuTree(Long userId) {
        // 获取用户的菜单列表
        List<SysMenu> userMenus = menuRepository.findMenusByUserId(userId);
        
        // 获取所有菜单，用于构建完整的树形结构
        List<SysMenu> allMenus = menuRepository.findAll().stream()
                .filter(m -> !m.getIsDeleted() && m.getStatus() == 1)
                .collect(Collectors.toList());
        
        // 获取用户有权限的菜单ID集合
        Set<Long> userMenuIds = userMenus.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());
        
        // 构建菜单ID到菜单对象的映射
        Map<Long, SysMenu> menuMap = allMenus.stream()
                .collect(Collectors.toMap(SysMenu::getId, menu -> menu));
        
        // 递归获取所有父级菜单ID
        Set<Long> allVisibleMenuIds = new HashSet<>(userMenuIds);
        for (Long menuId : userMenuIds) {
            addParentMenuIds(menuId, menuMap, allVisibleMenuIds);
        }
        
        // 过滤出用户可见的菜单
        List<SysMenu> visibleMenus = allMenus.stream()
                .filter(menu -> allVisibleMenuIds.contains(menu.getId()))
                .collect(Collectors.toList());

        // 转换为DTO
        List<MenuDTO> dtoList = visibleMenus.stream().map(m -> {
            MenuDTO dto = new MenuDTO();
            BeanUtils.copyProperties(m, dto);
            return dto;
        }).collect(Collectors.toList());

        // 构建树形结构
        Map<Long, List<MenuDTO>> parentMap = dtoList.stream()
                .collect(Collectors.groupingBy(MenuDTO::getParentId));

        // 获取顶级菜单
        List<MenuDTO> rootMenus = parentMap.getOrDefault(0L, new ArrayList<>());

        // 递归设置子菜单
        rootMenus.forEach(root -> setChildren(root, parentMap));

        return rootMenus;
    }
    
    /**
     * 递归添加父级菜单ID
     */
    private void addParentMenuIds(Long menuId, Map<Long, SysMenu> menuMap, Set<Long> visibleMenuIds) {
        SysMenu menu = menuMap.get(menuId);
        if (menu != null && menu.getParentId() != null && menu.getParentId() > 0) {
            visibleMenuIds.add(menu.getParentId());
            addParentMenuIds(menu.getParentId(), menuMap, visibleMenuIds);
        }
    }

    @Override
    @Transactional
    public void toggleMenuStatus(Long id) {
        SysMenu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("菜单不存在"));

        // 检查是否为核心菜单，核心菜单不允许禁用
        if (isCoreMenu(menu.getMenuCode()) && menu.getStatus() == 1) {
            throw new BusinessException("系统核心菜单不允许禁用");
        }

        // 切换状态
        menu.setStatus(menu.getStatus() == 1 ? 0 : 1);
        menu.setUpdateTime(LocalDateTime.now());
        menuRepository.save(menu);
    }
} 