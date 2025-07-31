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
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuRepository menuRepository;

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

        // 转换为DTO
        List<MenuDTO> dtoList = userMenus.stream().map(m -> {
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
} 