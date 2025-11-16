package com.myplaylist.app.dtos.video;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddVideoDTO {

    private String title;
    private String url;
    private String description;

}
