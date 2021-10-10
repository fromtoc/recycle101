/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.xin.portal.system.util.line.bean;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.linecorp.bot.client.LineClientConstants;
import com.xin.portal.system.util.line.bean.BotPropertiesValidator.ValidBotProperties;

@Validated
@ValidBotProperties
public class LineBotProperties {
	
	private String channelId;
    /**
     * Channel token supply mode.
     *
     * @see ChannelTokenSupplyMode
     */
    private @NotNull ChannelTokenSupplyMode channelTokenSupplyMode = ChannelTokenSupplyMode.FIXED;

    /**
     * Channel acccess token.
     */
    private @Valid String channelToken;

    /**
     * Channel secret.
     */
    private @Valid @NotNull String channelSecret;

    private @Valid @NotNull String apiEndPoint = LineClientConstants.DEFAULT_API_END_POINT;

    /**
     * Connection timeout in milliseconds.
     */
    private @Valid @NotNull long connectTimeout = LineClientConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * Read timeout in milliseconds.
     */
    private @Valid @NotNull long readTimeout = LineClientConstants.DEFAULT_READ_TIMEOUT_MILLIS;

    /**
     * Write timeout in milliseconds.
     */
    private @Valid @NotNull long writeTimeout = LineClientConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

    /**
     * Configuration for {@link LineMessageHandler} and {@link EventMapping}.
     */
    private @Valid @NotNull Handler handler = new Handler();

    public static class Handler {
        /**
         * Flag to enable/disable {@link LineMessageHandler} and {@link EventMapping}.
         *
         * <p>Default: {@code true}
         */
        boolean enabled = true;

        /**
         * REST endpoint path of dispatcher.
         */
        @NotNull
        URI path = URI.create("/callback");

		public final boolean isEnabled() {
			return enabled;
		}

		public final void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public final URI getPath() {
			return path;
		}

		public final void setPath(URI path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return "Handler [enabled=" + enabled + ", path=" + path + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (enabled ? 1231 : 1237);
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Handler other = (Handler) obj;
			if (enabled != other.enabled)
				return false;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			return true;
		}

		public Handler(boolean enabled, @NotNull URI path) {
			super();
			this.enabled = enabled;
			this.path = path;
		}

		public Handler() {
			super();
		}
    }

    public enum ChannelTokenSupplyMode {
        /**
         * Use fixed channel token for public API user.
         */
        FIXED,

        /**
         * Supply channel token via channel token supplier for specific business partners.
         *
         * @see <a href="https://developers.line.me/en/reference/messaging-api/#issue-channel-access-token"
         *         >//developers.line.me/en/reference/messaging-api/#issue-channel-access-token</a>
         */
        SUPPLIER,
    }

    
    //lombok @Data 标签内容
	public final ChannelTokenSupplyMode getChannelTokenSupplyMode() {
		return channelTokenSupplyMode;
	}

	public final String getChannelId() {
		return channelId;
	}

	public final void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public final void setChannelTokenSupplyMode(ChannelTokenSupplyMode channelTokenSupplyMode) {
		this.channelTokenSupplyMode = channelTokenSupplyMode;
	}

	public final String getChannelToken() {
		return channelToken;
	}

	public final void setChannelToken(String channelToken) {
		this.channelToken = channelToken;
	}

	public final String getChannelSecret() {
		return channelSecret;
	}

	public final void setChannelSecret(String channelSecret) {
		this.channelSecret = channelSecret;
	}

	public final String getApiEndPoint() {
		return apiEndPoint;
	}

	public final void setApiEndPoint(String apiEndPoint) {
		this.apiEndPoint = apiEndPoint;
	}

	public final long getConnectTimeout() {
		return connectTimeout;
	}

	public final void setConnectTimeout(long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public final long getReadTimeout() {
		return readTimeout;
	}

	public final void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public final long getWriteTimeout() {
		return writeTimeout;
	}

	public final void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public final Handler getHandler() {
		return handler;
	}

	public final void setHandler(Handler handler) {
		this.handler = handler;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiEndPoint == null) ? 0 : apiEndPoint.hashCode());
		result = prime * result + ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result + ((channelSecret == null) ? 0 : channelSecret.hashCode());
		result = prime * result + ((channelToken == null) ? 0 : channelToken.hashCode());
		result = prime * result + ((channelTokenSupplyMode == null) ? 0 : channelTokenSupplyMode.hashCode());
		result = prime * result + (int) (connectTimeout ^ (connectTimeout >>> 32));
		result = prime * result + ((handler == null) ? 0 : handler.hashCode());
		result = prime * result + (int) (readTimeout ^ (readTimeout >>> 32));
		result = prime * result + (int) (writeTimeout ^ (writeTimeout >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineBotProperties other = (LineBotProperties) obj;
		if (apiEndPoint == null) {
			if (other.apiEndPoint != null)
				return false;
		} else if (!apiEndPoint.equals(other.apiEndPoint))
			return false;
		if (channelId == null) {
			if (other.channelId != null)
				return false;
		} else if (!channelId.equals(other.channelId))
			return false;
		if (channelSecret == null) {
			if (other.channelSecret != null)
				return false;
		} else if (!channelSecret.equals(other.channelSecret))
			return false;
		if (channelToken == null) {
			if (other.channelToken != null)
				return false;
		} else if (!channelToken.equals(other.channelToken))
			return false;
		if (channelTokenSupplyMode != other.channelTokenSupplyMode)
			return false;
		if (connectTimeout != other.connectTimeout)
			return false;
		if (handler == null) {
			if (other.handler != null)
				return false;
		} else if (!handler.equals(other.handler))
			return false;
		if (readTimeout != other.readTimeout)
			return false;
		if (writeTimeout != other.writeTimeout)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LineBotProperties [channelId=" + channelId + ", channelTokenSupplyMode=" + channelTokenSupplyMode
				+ ", channelToken=" + channelToken + ", channelSecret=" + channelSecret + ", apiEndPoint=" + apiEndPoint
				+ ", connectTimeout=" + connectTimeout + ", readTimeout=" + readTimeout + ", writeTimeout="
				+ writeTimeout + ", handler=" + handler + "]";
	}

	public LineBotProperties(String channelId, @NotNull ChannelTokenSupplyMode channelTokenSupplyMode,
			@Valid String channelToken, @Valid @NotNull String channelSecret, @Valid @NotNull String apiEndPoint,
			@Valid @NotNull long connectTimeout, @Valid @NotNull long readTimeout, @Valid @NotNull long writeTimeout,
			@Valid @NotNull Handler handler) {
		super();
		this.channelId = channelId;
		this.channelTokenSupplyMode = channelTokenSupplyMode;
		this.channelToken = channelToken;
		this.channelSecret = channelSecret;
		this.apiEndPoint = apiEndPoint;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.writeTimeout = writeTimeout;
		this.handler = handler;
	}

	public LineBotProperties(String channelId, @Valid String channelToken, @Valid @NotNull String channelSecret) {
		super();
		this.channelId = channelId;
		this.channelToken = channelToken;
		this.channelSecret = channelSecret;
	}

	public LineBotProperties() {
		super();
	}
	
	
    
}
