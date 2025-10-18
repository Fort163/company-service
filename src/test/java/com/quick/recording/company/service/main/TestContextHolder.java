package com.quick.recording.company.service.main;

import com.quick.recording.gateway.dto.BaseDto;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TestContextHolder {

    private final HashMap<String, LinkedList<? super BaseDto>> currentMap;

    private HashMap<String, ? super BaseDto> lastDeletedElement;

    public TestContextHolder() {
        this.currentMap = new HashMap<>();
        this.lastDeletedElement = new HashMap<>();
    }

    public boolean put(String key, BaseDto value) {
        if(this.currentMap.containsKey(key)){
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), value.getUuid());
            if(indexByUUID > -1){
                this.currentMap.get(key).remove(indexByUUID);
                this.currentMap.get(key).add(indexByUUID, value);
                return true;
            }
            else {
                return this.currentMap.get(key).add(value);
            }
        }
        else {
            LinkedList<BaseDto> list = new LinkedList<>();
            list.add(value);
            return Objects.nonNull(this.currentMap.put(key, list));
        }
    }

    public boolean remove(String key, BaseDto value) {
        if(this.currentMap.containsKey(key)){
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), value.getUuid());
            if(indexByUUID > -1){
                BaseDto remove = (BaseDto) this.currentMap.get(key).remove(indexByUUID);
                this.lastDeletedElement.put(key, remove);
                return true;
            }
        }
        return false;
    }

    @Nullable
    public BaseDto getLastDeleted(String key){
        return (BaseDto) this.lastDeletedElement.get(key);
    }

    @Nullable
    public BaseDto getByNumber(String key, int number){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            return (BaseDto) this.currentMap.get(key).get(number);
        }
        return null;
    }

    @Nullable
    public BaseDto getByUUID(String key, UUID uuid){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)) {
            int indexByUUID = findIndexByUUID(this.currentMap.get(key), uuid);
            if (indexByUUID > -1) {
                return (BaseDto) this.currentMap.get(key).get(indexByUUID);
            }
        }
        return null;
    }

    @Nullable
    public BaseDto getLast(String key){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            try {
                return (BaseDto) this.currentMap.get(key).getLast();
            }
            catch (NoSuchElementException exception){
                return null;
            }
        }
        return null;
    }

    @Nullable
    public <T extends BaseDto> List<T> getList(String key){
        LinkedList<? super BaseDto> objects = this.currentMap.get(key);
        if(Objects.nonNull(objects)){
            return this.currentMap.get(key).stream().map(item -> (T) item).collect(Collectors.toList());
        }
        return null;
    }
    
    public int findIndexByUUID(List<? super BaseDto> list, UUID uuid) {
        OptionalInt index = IntStream.range(0, list.size())
                .filter(i -> ((BaseDto) list.get(i)).getUuid().equals(uuid))
                .findFirst();
        return index.isPresent() ? index.getAsInt() : -1;
    }

}
